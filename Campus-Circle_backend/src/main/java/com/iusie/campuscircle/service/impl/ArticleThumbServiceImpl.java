package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.model.entity.ArticleThumb;
import com.iusie.campuscircle.service.ArticleThumbService;
import com.iusie.campuscircle.mapper.ArticleThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【article_thumb(文章点赞)】的数据库操作Service实现
* @createDate 2024-12-02 17:20:02
*/
@Service
public class ArticleThumbServiceImpl extends ServiceImpl<ArticleThumbMapper, ArticleThumb>
    implements ArticleThumbService{

    @Override
    public boolean isArticleThumb(Long articleId, Long UserId) {
        return false;
    }
}




