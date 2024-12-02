package com.iusie.campuscircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iusie.campuscircle.annotation.OperationLogger;
import com.iusie.campuscircle.common.*;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.request.user.UpdateUserRequest;
import com.iusie.campuscircle.model.request.user.UserLoginRequest;
import com.iusie.campuscircle.model.request.user.UserRegisterRequest;
import com.iusie.campuscircle.model.vo.LoginResponse;
import com.iusie.campuscircle.model.vo.UserVO;
import com.iusie.campuscircle.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BaseResponse<LoginResponse> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        if (userLoginRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "请求头为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号或密码为空");
        }
        LoginResponse result = userService.userLogin(userAccount, userPassword, response);
        return ResultUtils.success(result);
    }

    @Operation(summary = "用户退出")
    @OperationLogger("用户退出")
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int logout = userService.userLogout(request);
        return ResultUtils.success(logout);
    }

    @Operation(summary = "修改用户信息")
    @OperationLogger("修改用户信息")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        if (updateUserRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        boolean result = userService.updateUser(updateUserRequest, request);
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取单个用户信息")
    @OperationLogger("获取单个用户信息")
    @GetMapping("/getUserInfo")
    public BaseResponse<UserDO> getUserInfo(@RequestParam Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loggingUser = userService.getLoggingUser(request);
        UserDO result = userService.getUserInfoById(id, loggingUser);
        result.setUserPassword(null);
        return ResultUtils.success(result);
    }


    @Operation(summary = "查询用户")
    @OperationLogger("查询用户")
    @GetMapping("/searchUsers")
    public BaseResponse<List<UserVO>> searchUsers(String userAccount, String userName, HttpServletRequest request) {
        if (userAccount == null && userName == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loggingUser = userService.getLoggingUser(request);
        List<UserVO> userList = userService.searchUsers(userAccount, userName, loggingUser);
        return ResultUtils.success(userList);
    }

    @Operation(summary = "用户列表(管理员)")
    @OperationLogger("查询用户列表")
    @PostMapping("/usersList")
    public BaseResponse<Page<User>> usersList(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        long current = pageRequest.getCurrent();
        long size = pageRequest.getPageSize();
        if (!userService.isAdmin(request)) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        Page<User> userPage = userService.page(new Page<>(current, size), userService.getQueryWrapper());
        return ResultUtils.success(userPage);
    }

    @Operation(summary = "删除用户")
    @OperationLogger("删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        boolean delete = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(delete);
    }

}
