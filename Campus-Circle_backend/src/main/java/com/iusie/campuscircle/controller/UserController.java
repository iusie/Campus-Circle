package com.iusie.campuscircle.controller;

import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.vo.UserRegisterRequest;
import com.iusie.campuscircle.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
