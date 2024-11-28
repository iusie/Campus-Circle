package com.iusie.campuscircle.model.request.team;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author iusie
 * @description 队伍更新封装类
 * @date 2024/11/28
 */
@Data
public class TeamUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8633356126420546002L;

    private Long id;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍头像
     */
    private String avatarTeam;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍密码
     */
    private String teamPassword;

    /**
     * 状态 0-正常 1-私有  2-加密
     */
    private Integer teamState;

    /**
     * 过期时间
     */
    private Date expireTime;


}