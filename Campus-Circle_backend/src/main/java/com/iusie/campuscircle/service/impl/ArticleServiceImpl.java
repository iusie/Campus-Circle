package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.manager.RedisService;
import com.iusie.campuscircle.mapper.ArticleMapper;
import com.iusie.campuscircle.model.converter.ArticleConverter;
import com.iusie.campuscircle.model.converter.UserConverter;
import com.iusie.campuscircle.model.dto.ArticleDO;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Article;
import com.iusie.campuscircle.model.entity.ArticleComment;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.model.request.article.ArticleUpdateRequest;
import com.iusie.campuscircle.model.vo.ArticleCommentVO;
import com.iusie.campuscircle.model.vo.ArticleVO;
import com.iusie.campuscircle.service.ArticleCommentService;
import com.iusie.campuscircle.service.ArticleService;
import com.iusie.campuscircle.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author admin
 * @description 针对表【article(文章)】的数据库操作Service实现
 * @createDate 2024-12-03 09:30:32
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Resource
    RedisService redisService;

    @Resource
    UserService userService;

    @Resource
    ArticleCommentService articleCommentService;

    private void validateHeadline(String headline) {
        //文章标题不能为空且大于5个字小于100个字
        if (StringUtils.isBlank(headline)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "文章标题不能为空");
        }
        if (headline.length() < 5 || headline.length() > 100) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "文章标题不符合要求");
        }
    }

    private void validateContent(String content) {
        //文章内容不能为空且大于10个字
        if (StringUtils.isBlank(content) || content.length() < 10) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "内容不符合要求");
        }
    }

    /**
     * 添加文章
     *
     * @param article
     * @param loginUser
     * @return
     */
    @Override
    public long addArticle(Article article, UserDO loginUser) {
        //请求参数是否为空
        if (article == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        //用户是否登录
        if (loginUser == null) {
            throw new BusinessException(StateCode.NOT_LOGIN_ERROR);
        }
        final long userId = loginUser.getId();
        validateHeadline(article.getHeadline());
        String content = article.getContent();
        validateContent(content);
        //如果文章概括为空,截取文章内容前25位字符
        String summarized = article.getSummarized();
        if (StringUtils.isBlank(summarized)) {
            summarized = content.substring(0, Math.min(content.length(), 25));
            article.setSummarized(summarized);
        }
        //todo 判断敏感字符、图片、标签
        article.setId(null);
        article.setUserId(userId);
        if (article.getCoverUrl() == null) {
            article.setCoverUrl("https://jsd.onmicrosoft.cn/gh/iusie/image/miu.png");
        }
        boolean result = this.save(article);

        Long articleId = article.getId();
        if (!result || articleId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "发布帖子失败");
        }
        //缓存文章
        ArticleDO articleDO = ArticleConverter.convertToArticleDO(article);
        redisService.ArticleCache(articleId, articleDO);

        return articleId;
    }

    /**
     * 删除文章
     *
     * @param id        文章id
     * @param loginUser 用户信息
     * @return true or false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(Long id, UserDO loginUser) {
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "文章不存在");
        }
        Long userId = article.getUserId();
        if (userId == null) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "文章用户ID获取失败");
        }
        //是否为文章创建者或管理员
        if (!userService.isAdmin(loginUser) && !userId.equals(loginUser.getId())) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("articleId", id);
        //删除帖子的评论。如果没有评论则不删除
        long articleCommentCount = articleCommentService.count(queryWrapper);
        if (articleCommentCount > 0) {
            boolean remove = articleCommentService.remove(queryWrapper);
            // todo 删除评论的缓存
            if (!remove) {
                throw new BusinessException(StateCode.SYSTEM_ERROR, "删除帖子评论失败");
            }
        }
        //删除文章以及文章的缓存信息
        boolean remove = this.removeById(id);
        redisService.removeArticleCache(id);
        return remove;
    }

    /**
     * 更新文章
     *
     * @param articleUpdateRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean updateArticle(ArticleUpdateRequest articleUpdateRequest, UserDO loginUser) {
        Article oldArticle = this.getById(articleUpdateRequest.getId());
        if (oldArticle == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "文章不存在");
        }
        Long userId = oldArticle.getUserId();
        //是否为为文章创建人或者管理员
        if (!userService.isAdmin(loginUser) && !userId.equals(loginUser.getId())) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR);
        }
        String headline = articleUpdateRequest.getHeadline();
        validateHeadline(headline);
        String content = articleUpdateRequest.getContent();
        validateContent(content);
        //如果文章概括为空,截取文章内容前25位字符
        String summarized = articleUpdateRequest.getSummarized();
        if (StringUtils.isBlank(summarized)) {
            summarized = content.substring(0, Math.min(content.length(), 25));
            articleUpdateRequest.setSummarized(summarized);
        }
        if (articleUpdateRequest.getCoverUrl() == null) {
            articleUpdateRequest.setCoverUrl("https://jsd.onmicrosoft.cn/gh/iusie/image/miu.png");
        }
        //todo 判断敏感字符、图片、标签
        Article article = new Article();
        //更新数据库内容后更新缓存
        BeanUtils.copyProperties(articleUpdateRequest, article);
        ArticleDO articleDO = ArticleConverter.convertToArticleDO(article);
        boolean b = this.updateById(article);
        articleDO.setUserId(userId);
        redisService.ArticleCache(articleUpdateRequest.getId(), articleDO);

        return b;
    }

    /**
     * 查看文章
     *
     * @param id
     * @param b
     * @return
     */
    @Override
    public ArticleVO getArticleById(long id, boolean b) {
        //缓存文章不为空
        ArticleDO articleCache = redisService.getArticleCache(id);
        if (articleCache != null) {
            //帖子脱敏
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(articleCache, articleVO);
            Long createUserId = articleVO.getUserId();
            System.out.println(articleCache);
            System.out.println(createUserId);
            UserDO createUserCache = redisService.getUserInfoCache(createUserId);
            System.out.println(createUserCache);
            if (createUserCache != null) {
                articleVO.setUsername(createUserCache.getUsername());
                articleVO.setAvatarUrl(createUserCache.getAvatarUrl());
            } else {
                //查帖子的创建人信息
                User createUser = userService.getById(articleVO.getUserId());
                articleVO.setUsername(createUser.getUsername());
                articleVO.setAvatarUrl(createUser.getAvatarUrl());
            }
            List<ArticleCommentVO> articleCommentVOListCache = redisService.getArticleCommentCache(articleVO.getId());
            if (articleCommentVOListCache != null) {
                articleVO.setArticleComments(articleCommentVOListCache);
            } else {
                List<ArticleCommentVO> articleCommentVOList = articleCommentService.getarticleCommentVOList(articleVO.getId());
                articleVO.setArticleComments(articleCommentVOList);
            }
            return articleVO;
        }

        //缓存文章为空
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "贴子不存在");
        }
        //帖子脱敏
        ArticleVO articleVO = new ArticleVO();
        ArticleDO articleDO = ArticleConverter.convertToArticleDO(article);
        BeanUtils.copyProperties(articleDO, articleVO);
        //查帖子的创建人信息
        User createUser = userService.getById(articleVO.getUserId());
        UserDO userDO = UserConverter.convertToUserDO(createUser);
        articleVO.setUsername(createUser.getUsername());
        articleVO.setAvatarUrl(createUser.getAvatarUrl());
        //查询帖子评论
        Long articleVOId = articleVO.getId();
        List<ArticleCommentVO> articleCommentVOList = articleCommentService.getarticleCommentVOList(articleVOId);
        articleVO.setArticleComments(articleCommentVOList);

        redisService.ArticleCache(id, articleDO);
        redisService.UserInfoCache(articleVO.getUserId(),userDO);
        redisService.articleCommentVOList(id,articleCommentVOList);

        return articleVO;
    }


}




