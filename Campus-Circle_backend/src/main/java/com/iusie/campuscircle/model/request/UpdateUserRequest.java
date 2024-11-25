package com.iusie.campuscircle.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author iusie
 * @description
 * @date 2024/11/25
 */
@Data
public class UpdateUserRequest  implements Serializable {

    @Serial
    private static final long serialVersionUID = 8541538311214997955L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String surePassword;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户自我介绍
     */
    private String userProfile;

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

    /**
     * 标签 json 列表
     */
    private String tags;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

}
