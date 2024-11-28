package com.iusie.campuscircle.controller;

import com.iusie.campuscircle.annotation.OperationLogger;
import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.DeleteRequest;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.entity.Team;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.request.team.TeamAddRequest;
import com.iusie.campuscircle.model.request.team.TeamUpdateRequest;
import com.iusie.campuscircle.model.vo.TeamVO;
import com.iusie.campuscircle.service.TeamService;
import com.iusie.campuscircle.service.UserService;
import com.iusie.campuscircle.service.UserTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author iusie
 * @description
 * @date 2024/11/27
 */
@RestController
@RequestMapping("/team")
@Slf4j
@Tag(name = "队伍接口")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    private final UserService userService;

    private final UserTeamService userTeamService;

    @OperationLogger("创建队伍")
    @Operation(summary = "创建队伍")
    @PostMapping("/createTeam")
    public BaseResponse<Long> createTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoggingUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.createTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    @OperationLogger("解散队伍")
    @Operation(summary = "解散队伍")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest httpServletRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        Long teamId = deleteRequest.getId();
        if (teamId <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoggingUser(httpServletRequest);
        boolean result = teamService.deleteTeam(teamId, loginUser);
        if (!result) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param httpServletRequest
     * @return
     */

    @OperationLogger("更新队伍")
    @Operation(summary = "更新队伍")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest httpServletRequest) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoggingUser(httpServletRequest);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "数据更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 查询队伍
     *
     * @param id
     * @return
     */
    @OperationLogger("查询队伍")
    @Operation(summary = "查询队伍")
    @GetMapping("/get")
    public BaseResponse<TeamVO> getTeamById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        TeamVO team = teamService.getTeamInfoById(id);
        if (team == null) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "查询队伍失败");
        }
        return ResultUtils.success(team);
    }


}
