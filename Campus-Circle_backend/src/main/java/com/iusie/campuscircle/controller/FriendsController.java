package com.iusie.campuscircle.controller;

import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.request.friends.FriendAddRequest;
import com.iusie.campuscircle.service.FriendsService;
import com.iusie.campuscircle.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iusie
 * @description
 * @date 2024/12/13
 */

@RestController
@RequestMapping("/friends")
@Slf4j
@Tag(name = "好友申请接口")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    private final UserService userService;

    /**
     * 添加好友
     *
     * @param friendAddRequest 好友添加请求
     * @param request 请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    public BaseResponse<Boolean> addFriendRecords(@RequestBody FriendAddRequest friendAddRequest, HttpServletRequest request)
    {
        if (friendAddRequest == null)
        {
            throw new BusinessException(StateCode.PARAMS_ERROR,"请求错误");
        }
        UserDO loggingUser = userService.getLoggingUser(request);
        boolean records = friendsService.addFriendRecords(loggingUser, friendAddRequest);
        return ResultUtils.success(records);
    }

}
