package com.iusie.campuscircle.controller;

import com.iusie.campuscircle.common.BaseResponse;
import com.iusie.campuscircle.common.DeleteRequest;
import com.iusie.campuscircle.common.ResultUtils;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Article;
import com.iusie.campuscircle.model.request.article.ArticleAddRequest;
import com.iusie.campuscircle.model.request.article.ArticleUpdateRequest;
import com.iusie.campuscircle.model.vo.ArticleVO;
import com.iusie.campuscircle.service.ArticleService;
import com.iusie.campuscircle.service.ArticleThumbService;
import com.iusie.campuscircle.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author iusie
 * @description
 * @date 2024/12/2
 */

@RestController
@RequestMapping("/article")
@Slf4j
@Tag(name = "文章接口")
@RequiredArgsConstructor
public class ArticleController {

    private final UserService userService;

    private final ArticleService articleService;

    private final ArticleThumbService articleThumbService;

    /**
     * 添加文章
     *
     * @param articleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addArticle(@RequestBody ArticleAddRequest articleAddRequest, HttpServletRequest request) {
        if (articleAddRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loginUser = userService.getLoggingUser(request);
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        long articleId = articleService.addArticle(article, loginUser);
        return ResultUtils.success(articleId);
    }

    /**
     * 删除文章
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteArticle(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Long id = deleteRequest.getId();
        if (id <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loginUser = userService.getLoggingUser(request);
        boolean result = articleService.deleteArticle(id, loginUser);
        if (!result) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新文章
     *
     * @param articleUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest, HttpServletRequest request) {
        if (articleUpdateRequest == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loginUser = userService.getLoggingUser(request);
        boolean result = articleService.updateArticle(articleUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "数据更新失败");
        }
        return ResultUtils.success(true);
    }


    /**
     * 查询文章
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ArticleVO> getArticleById(@RequestParam long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        UserDO loginUser = userService.getLoggingUser(request);
        ArticleVO articleVO = articleService.getArticleById(id, true);
        articleVO.setThumb(articleThumbService.isArticleThumb(articleVO.getId(), loginUser.getId()));
        if (ObjectUtils.isEmpty(articleVO)) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "查询帖子失败");
        }
        return ResultUtils.success(articleVO);
    }

}
