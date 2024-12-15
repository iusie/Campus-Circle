package com.iusie.campuscircle.model.request.team;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author iusie
 * @description
 * @date 2024/11/27
 */
@Data
public class TeamAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8911934716308167050L;
    /**
     * 用户id
     */
    private Long userId;

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
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 队伍密码
     */
    private String teamPassword;

    /**
     * 队伍类型 1-竞赛 2-学习交流 2-休闲交友
     */
    private Integer  teamType;

    /**
     * 状态 0-正常 1-私有  2-加密
     */
    private Integer teamState;

    /**
     * 过期时间
     */
    private Date expireTime;


}