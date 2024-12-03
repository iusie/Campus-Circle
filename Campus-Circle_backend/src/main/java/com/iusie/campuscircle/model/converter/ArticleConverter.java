package com.iusie.campuscircle.model.converter;

import cn.hutool.json.JSONUtil;
import com.iusie.campuscircle.model.dto.ArticleDO;
import com.iusie.campuscircle.model.entity.Article;

import java.util.List;

/**
 * @author iusie
 * @description
 * @date 2024/12/2
 */
public class ArticleConverter {

    public static ArticleDO convertToArticleDO(Article article) {
        ArticleDO articleDO = new ArticleDO();

        articleDO.setId(article.getId());
        articleDO.setUserId(article.getUserId());
        articleDO.setHeadline(article.getHeadline());
        articleDO.setContent(article.getContent());
        articleDO.setSummarized(article.getSummarized());
        articleDO.setCoverUrl(article.getCoverUrl());
        articleDO.setArticleState(article.getArticleState());
        articleDO.setThumbNum(article.getThumbNum());
        articleDO.setCreateTime(article.getCreateTime());
        articleDO.setUpdateTime(article.getUpdateTime());
        articleDO.setIsDelete(article.getIsDelete());


        // 将 tags 字段从 JSON 字符串转换为 List<String>
        if (article.getArticleTags() != null) {
            List<String> articleTagsList = JSONUtil.toList(article.getArticleTags(), String.class);
            articleDO.setArticleTags(articleTagsList);
        }

        return articleDO;
    }
}
