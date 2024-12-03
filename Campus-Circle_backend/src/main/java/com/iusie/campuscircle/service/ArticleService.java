package com.iusie.campuscircle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Article;
import com.iusie.campuscircle.model.request.article.ArticleUpdateRequest;
import com.iusie.campuscircle.model.vo.ArticleVO;

/**
* @author admin
* @description 针对表【article(文章)】的数据库操作Service
* @createDate 2024-12-03 09:30:32
*/
public interface ArticleService extends IService<Article> {

    /**
     * 添加文章
     *
     * @param article
     * @param loginUser
     * @return
     */
    long addArticle(Article article, UserDO loginUser);

    /**
     * 删除文章
     *
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteArticle(Long id, UserDO loginUser);

    /**
     * 更新文章
     *
     * @param articleUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateArticle(ArticleUpdateRequest articleUpdateRequest, UserDO loginUser);

    /**
     * 查看文章
     *
     * @param id
     * @param b
     * @return
     */
    ArticleVO getArticleById(long id, boolean b);

}
