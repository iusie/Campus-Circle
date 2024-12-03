package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.entity.ArticleThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【article_thumb(文章点赞)】的数据库操作Service
* @createDate 2024-12-02 17:20:02
*/
public interface ArticleThumbService extends IService<ArticleThumb> {


    boolean isArticleThumb(Long articleId, Long UserId);
}
