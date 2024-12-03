package com.iusie.campuscircle.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author iusie
 * @description
 * @date 2024/12/2
 */
@Data
public class ArticleDO implements Serializable {
    /**
     * 文章id
     */
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
     * 内容
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
    private List<String> articleTags;

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
    private Integer isDelete;

    @Serial
    private static final long serialVersionUID = 5463481550050645983L;
}
