package com.iusie.campuscircle.model.request.article;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author iusie
 * @description
 * @date 2024/12/3
 */
@Data
public class ArticleAddRequest implements Serializable {

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
    private String articleTags;

    @Serial
    private static final long serialVersionUID = 2372255742215063507L;
}

