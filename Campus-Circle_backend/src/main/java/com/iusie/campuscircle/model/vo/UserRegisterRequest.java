package com.iusie.campuscircle.model.vo;

import lombok.Data;

/**
 * @author iusie
 * @description 用户注册请求实体
 * @date 2024/11/23
 */
@Data
public class UserRegisterRequest {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
