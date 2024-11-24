package com.iusie.campuscircle.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author iusie
 * @description 用户注册请求实体
 * @date 2024/11/23
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6511513391726229090L;
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
