package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.model.entity.ArticleComment;
import com.iusie.campuscircle.model.vo.ArticleCommentVO;
import com.iusie.campuscircle.service.ArticleCommentService;
import com.iusie.campuscircle.mapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author admin
* @description 针对表【article_comment(文章评论)】的数据库操作Service实现
* @createDate 2024-12-03 10:57:23
*/
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
    implements ArticleCommentService{

    @Override
    public List<ArticleCommentVO> getarticleCommentVOList(Long id) {
        return List.of();
    }
}




