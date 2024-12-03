package com.iusie.campuscircle.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;


    /**
     * 文章标题
     */
    private String headline;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章概括
     */
    private String summarized;

    /**
     * 文章封面
     */
    private String coverUrl;

    /**
     * 状态 0 正常
     */
    private Integer articleState;

    /**
     * 标签 json 列表
     */
    private String articleTags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}