package com.iusie.campuscircle.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 好友申请管理表
 * @TableName friends
 */
@TableName(value ="friends")
@Data
public class Friends implements Serializable {
    /**
     * 好友申请id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送申请的用户id
     */
    private Long fromId;

    /**
     * 接收申请的用户id 
     */
    private Long receiveId;

    /**
     * 是否已读(0-未读 1-已读)
     */
    private Integer isRead;

    /**
     * 申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-不同意）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 好友申请备注信息
     */
    private String remark;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}