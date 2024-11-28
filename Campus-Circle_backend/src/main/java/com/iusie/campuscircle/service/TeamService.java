package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.request.team.TeamUpdateRequest;
import com.iusie.campuscircle.model.vo.TeamVO;

/**
* @author admin
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-11-27 11:04:57
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team 队伍信息
     * @param loginUser 创建人信息
     * @return
     */
    long createTeam(Team team, User loginUser);

    /**
     * 解散队伍
     *
     * @param teamId 队伍id
     * @param loginUser 解散者信息
     * @return
     */
    boolean deleteTeam(long teamId, User loginUser);

    /**
     * 根据队伍的id解散队伍,移除队伍相关信息
     *
     * @param teamId 队伍id
     * @return
     */
    boolean removeByTeamId(long teamId);

    /**
     * 更新队伍信息
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 根据队伍Id查询队伍信息
     *
     * @param id
     * @return TeamVO
     */
    TeamVO getTeamInfoById(long id);
}
