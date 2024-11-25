package com.iusie.campuscircle.controller;

import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.request.UserLoginRequest;
import com.iusie.campuscircle.model.request.UserRegisterRequest;
import com.iusie.campuscircle.model.vo.UserVO;
import com.iusie.campuscircle.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iusie
 * @description
 * @date 2024/11/23
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "用户接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "不能提交空表单");
        }
        long userRegister = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(userRegister);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        if (userLoginRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "请求头为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号或密码为空");
        }
        UserVO result = userService.userLogin(userAccount, userPassword, response);
        return ResultUtils.success(result);
    }

    @Operation(summary = "用户退出")
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int logout = userService.userLogout(request);
        return ResultUtils.success(logout);
    }

}
