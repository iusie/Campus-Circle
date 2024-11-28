package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.entity.Team;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.entity.UserTeam;
import com.iusie.campuscircle.model.enums.TeamStatusEnums;
import com.iusie.campuscircle.model.request.team.TeamUpdateRequest;
import com.iusie.campuscircle.model.vo.TeamVO;
import com.iusie.campuscircle.model.vo.UserVO;
import com.iusie.campuscircle.service.TeamService;
import com.iusie.campuscircle.mapper.TeamMapper;
import com.iusie.campuscircle.service.UserService;
import com.iusie.campuscircle.service.UserTeamService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    /**
     * 创建队伍
     *
     * @param team      队伍信息
     * @param loginUser 穿贱人信息
     * @return
     */
    @Override
    public long createTeam(Team team, User loginUser) {
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
    public boolean deleteTeam(long teamId, User loginUser) {
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
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
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


}




