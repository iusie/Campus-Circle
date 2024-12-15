package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.constant.RedissonConstant;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.converter.UserConverter;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Team;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.entity.UserTeam;
import com.iusie.campuscircle.model.enums.TeamStatusEnums;
import com.iusie.campuscircle.model.request.team.TeamJoinRequest;
import com.iusie.campuscircle.model.request.team.TeamUpdateRequest;
import com.iusie.campuscircle.model.vo.TeamVO;
import com.iusie.campuscircle.model.vo.UserVO;
import com.iusie.campuscircle.service.TeamService;
import com.iusie.campuscircle.mapper.TeamMapper;
import com.iusie.campuscircle.service.UserService;
import com.iusie.campuscircle.service.UserTeamService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author admin
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-11-27 11:04:57
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    private UserService userService;

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private RedissonClient redissonService;

    /**
     * 创建队伍
     *
     * @param team      队伍信息
     * @param loginUser 穿贱人信息
     * @return
     */
    @Override
    public long createTeam(Team team, UserDO loginUser) {
        //请求参数是否为空
        if (team == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        //用户是否登录
        if (loginUser == null) {
            throw new BusinessException(StateCode.NOT_LOGIN_ERROR);
        }
        //队伍最大人数不大于20不小于1
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //队伍名称 <=20
        String teamName = team.getTeamName();
        if (StringUtils.isNotBlank(teamName) && teamName.length() > 20) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍名不满足要求");
        }
        //描述小于128
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 128) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍描述不满足要求");
        }
        //队伍类型必填
        Integer teamType = team.getTeamType();
        if (teamType == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍类型为空");
        }
        // teamStatue 0为公开 默认为0
        int statue = Optional.ofNullable(team.getTeamState()).orElse(0);
        TeamStatusEnums teamStatusEnums = TeamStatusEnums.getEnumByValues(statue);
        if (teamStatusEnums == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        // teamStatue 为加密状态 需要密码，密码<=32
        String teamPassword = team.getTeamPassword();
        if (TeamStatusEnums.SECRET.equals(teamStatusEnums) && (StringUtils.isBlank(teamPassword) || teamPassword.length() > 32)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍加密不满足要求");
        }
        //超时时间大于当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "超时时间小于当前时间");
        }
        //校验用户最多创建10个队伍
        final long userId = loginUser.getId();
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long teamCount = this.count(queryWrapper);
        if (teamCount >= 10) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "队伍数量不能大于5");
        }
        //信息插入队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "创建队伍失败");
        }

        //信息插入队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    /**
     * 解散队伍
     *
     * @param teamId    队伍id
     * @param loginUser 解散者信息
     * @return
     */
    @Override
    public boolean deleteTeam(long teamId, UserDO loginUser) {
        Team team = getTeamById(teamId);
        //是否为队长
        long userId = loginUser.getId();
        if (team.getUserId() != userId || !userService.isAdmin(loginUser)) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        //移除队伍
        return removeByTeamId(teamId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByTeamId(long teamId) {
        QueryWrapper<UserTeam> teamIdQueryWrapper = new QueryWrapper<>();
        teamIdQueryWrapper.eq("teamId", teamId);
        boolean result = userTeamService.remove(teamIdQueryWrapper);
        if (!result) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "删除队伍中用户失败");
        }
        return this.removeById(teamId);
    }

    /**
     * 根据队伍teamId获取队伍信息
     *
     * @param teamId
     * @return
     */
    private Team getTeamById(long teamId) {
        if (teamId <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(StateCode.NOT_FOUND_ERROR, "队伍不存在");
        }
        return team;
    }

    /**
     * 更新队伍信息
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, UserDO loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "更新数据不能为空");
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(StateCode.NOT_FOUND_ERROR, "队伍不存在");
        }
        //只要管理员和队长才能修改
        if (!oldTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        TeamStatusEnums teamStatusEnums = TeamStatusEnums.getEnumByValues(teamUpdateRequest.getTeamState());
        if (teamStatusEnums.equals(TeamStatusEnums.SECRET)) {
            if (StringUtils.isBlank(teamUpdateRequest.getTeamPassword())) {
                throw new BusinessException(StateCode.PARAMS_ERROR, "加密房间需要设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        return this.updateById(updateTeam);
    }

    /**
     * 根据队伍Id查询队伍信息
     *
     * @param id 队伍id
     * @return TeamVO
     */
    @Override
    public TeamVO getTeamInfoById(long id) {
        Team team = this.getById(id);
        Long userId = team.getUserId();
        if (userId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        User createUser = userService.getById(userId);
        TeamVO teamVO = new TeamVO();
        //脱敏信息
        if (createUser != null) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(createUser, userVO);
            teamVO.setCreateUser(userVO);
        }
        BeanUtils.copyProperties(team, teamVO);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", id);
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        //获取加入队伍的用户id
        List<Long> userIdList = userTeamList.stream().map(UserTeam::getUserId).collect(Collectors.toList());
        //根据用户id查出详细的信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        List<User> userList = userService.list(userQueryWrapper);
        //用户脱敏
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        }
        teamVO.setUserJoinList(userVOList);
        teamVO.setHasJoinNum(userVOList.size());
        return teamVO;
    }

    /**
     * 根据队伍类型查询队伍信息
     *
     * @param teamType
     * @return
     */
    @Override
    public List<TeamVO> getTeamInfoByType(Integer teamType) {
        List<TeamVO> teamVOList = new ArrayList<>();
        QueryWrapper<Team> teamQueryWrapper = new QueryWrapper<>();
        List<Team> teamList = this.list();
        if (teamType != null) {
            teamQueryWrapper.eq("teamType", teamType);
            teamList = this.list(teamQueryWrapper);
        }
        // 给 TeamVO 赋值
        for (Team team : teamList) {
            // 写入 队伍队长 信息
            TeamVO teamVO = new TeamVO();
            BeanUtils.copyProperties(team, teamVO);
            Long CreateUserId = teamVO.getUserId();
            User user = userService.getById(CreateUserId);
            UserDO userDO = UserConverter.convertToUserDO(user);
            UserVO CreateUse = new UserVO();
            BeanUtils.copyProperties(userDO, CreateUse);
            teamVO.setCreateUser(CreateUse);

            // 写入 队伍队员 信息
            // 查询一个队伍中人数列表的条件
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            Long teamId = team.getId();
            userTeamQueryWrapper.ne("userId", CreateUserId);
            userTeamQueryWrapper.eq("teamId", teamId);
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);

            // 通过关系表获得每一个队伍成员的信息并进行脱敏
            List<UserVO> teamUserList = new ArrayList<>();
            for (UserTeam TeamMember : userTeamList) {
                Long userId = TeamMember.getUserId();
                User teamUser = userService.getById(userId);
                UserDO teamUserDO = UserConverter.convertToUserDO(teamUser);
                UserVO teamUserVO = new UserVO();
                BeanUtils.copyProperties(teamUserDO, teamUserVO);
                teamUserList.add(teamUserVO);
            }
            teamVO.setUserJoinList(teamUserList);
            teamVO.setHasJoinNum(userTeamList.size());
            teamVOList.add(teamVO);
        }

        return teamVOList;
    }

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, UserDO loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "加入队伍异常");
        }
        Long teamId = teamJoinRequest.getTeamId();
        Team team = this.getById(teamId);
        if (teamId == null || teamId <= 0 || team == null) {
            throw new BusinessException(StateCode.NOT_FOUND_ERROR, "队伍不存在");
        }
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(StateCode.NOT_FOUND_ERROR, "队伍已过期");
        }
        TeamStatusEnums teamStatusEnums = TeamStatusEnums.getEnumByValues(team.getTeamState());
        if (teamStatusEnums.equals(TeamStatusEnums.PRIVATE)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "不能加入私有队伍");
        }
        String teamPassword = teamJoinRequest.getTeamPassword();
        if (TeamStatusEnums.SECRET.equals(teamStatusEnums)) {
            if (StringUtils.isBlank(teamPassword) || !team.getTeamPassword().equals(teamPassword)) {
                throw new BusinessException(StateCode.PARAMS_ERROR, "密码错误");
            }
        }
        // 加入队伍的用户ID
        Long userId = loginUser.getId();
        //只有一个线程抢到加入该队伍的锁
        RLock lock = redissonService.getLock(RedissonConstant.JOIN_TEAM_KEY + teamId);
        try {
            //反复抢锁
            while (true) {
                if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                    QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                    userTeamQueryWrapper.eq("userId", userId);
                    long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
                    if (hasJoinNum > 20) {
                        throw new BusinessException(StateCode.PARAMS_ERROR, "最多加入或者创建20个队伍");
                    }
                    //不能重复加入队伍
                    userTeamQueryWrapper.eq("teamId", teamId);
                    long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
                    if (hasUserJoinTeam > 0) {
                        throw new BusinessException(StateCode.OPERATION_ERROR, "用户已加入该队伍");
                    }
                    //已经加入队伍数量
                    long hasTeamJoinNum = getJoinTeamCount(teamId);
                    if (hasTeamJoinNum >= team.getMaxNum()) {
                        throw new BusinessException(StateCode.PARAMS_ERROR, "队伍已满");
                    }
                    //修改队伍信息
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
            log.error("加入队伍异常", e);
            return false;
        } finally {
            //释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 获取队伍中的人数
     *
     * @param teamId
     * @return
     */
    private long getJoinTeamCount(long teamId) {
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        return userTeamService.count(userTeamQueryWrapper);
    }

}




