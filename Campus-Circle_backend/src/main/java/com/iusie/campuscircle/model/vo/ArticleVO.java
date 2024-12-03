package com.iusie.campuscircle.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author iusie
 * @description
 * @date 2024/12/3
 */
@Data
public class ArticleVO implements Serializable {
    /**
     * 帖子id
     */
    private Long id;

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
     * 标签 json 列表
     */
    private List<String> articleTags;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String username;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 状态 0 正常
     */
    private Integer postState;

    /**
     * 点赞数
     */
    private Integer thumbNum;


    /**
     * 当前用户是否点赞
     */
    private boolean isThumb;

    /**
     * 文章的评论
     */
    private List<ArticleCommentVO> articleComments;

}
