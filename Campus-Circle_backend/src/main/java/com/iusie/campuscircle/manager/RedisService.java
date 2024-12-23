package com.iusie.campuscircle.manager;

import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.dto.ArticleDO;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.vo.ArticleCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.iusie.campuscircle.constant.UserConstant.EXPIRE_TIME;

/**
 * @Description: Redis配置
 * @Author: iusie
 * @Date: 2024/10/9 21:26
 */
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisService;

    // 保存token
    public void saveToken(Long userId, String token) {
        String tokenKey = "token:" + userId;
        ValueOperations<String, Object> operations = redisService.opsForValue();
        operations.set(tokenKey, token, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // 删除token
    public boolean removeToken(Long userId) {
        String tokenKey = "token:" + userId;
        return redisService.delete(tokenKey) != null;
    }

    // 获取token
    public String getToken(Long userId) {
        String tokenKey = "token:" + userId;  // 使用相同的键格式
        ValueOperations<String, Object> operations = redisService.opsForValue();
        return (String) operations.get(tokenKey);
    }

    // 进行登录用户信息缓存
    public void UserInfoCache(Long userId, UserDO userDO) {
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        operations.put(userCacheKey, userKey, userDO);
        redisService.expire(userCacheKey, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // 得到登录用户缓存信息
    public UserDO getUserInfoCache(Long userId) {
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        UserDO userDO = (UserDO) operations.get(userCacheKey, userKey);
        if (userId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户缓存异常");
        }
        return userDO;
    }

    // 删除用户缓存信息
    public void removeUserInfoCache(Long userId) {
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        if (userId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户Id不存在");
        }
        // 从 Redis 删除 UserVO
        operations.delete(userCacheKey, userKey);
    }

    // 文章缓存
    public void ArticleCache(Long articleId, ArticleDO articleDO) {
        String articleCacheKey = "ArticleCache:";
        String articleKey = "ArticleId:" + articleId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        operations.put(articleCacheKey, articleKey, articleDO);
        redisService.expire(articleCacheKey, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // 得到文章缓存信息
    public ArticleDO getArticleCache(Long articleId) {
        String articleCacheKey = "ArticleCache:";
        String articleKey = "ArticleId:" + articleId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        ArticleDO articleDO = (ArticleDO) operations.get(articleCacheKey, articleKey);
        if (articleId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户缓存异常");
        }
        return articleDO;
    }

    // 删除文章缓存信息
    public void removeArticleCache(Long articleId) {
        String articleCacheKey = "ArticleCache:";
        String articleKey = "ArticleId:" + articleId;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        if (articleId == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户Id不存在");
        }
        // 从 Redis 删除 UserVO
        operations.delete(articleCacheKey, articleKey);
    }

    public List<ArticleCommentVO> getArticleCommentCache(Long id) {
        return null;
    }

    public void articleCommentVOList(long id, List<ArticleCommentVO> articleCommentVOList) {
    }
}
