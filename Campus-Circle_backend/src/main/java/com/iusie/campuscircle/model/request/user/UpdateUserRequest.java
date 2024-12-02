package com.iusie.campuscircle.model.request.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
     * 用户账号
     */
    private String userAccount;


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


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
