package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.entity.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.vo.ArticleCommentVO;

import java.util.List;

/**
* @author admin
* @description 针对表【article_comment(文章评论)】的数据库操作Service
* @createDate 2024-12-03 10:57:23
*/
public interface ArticleCommentService extends IService<ArticleComment> {

    List<ArticleCommentVO> getarticleCommentVOList(Long id);
}
