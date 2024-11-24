package com.iusie.campuscircle.manager;

import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

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
    public void saveToken(String userAccount ,String token) {
        String tokenKey = "token:" + userAccount;
        ValueOperations<String,Object> operations = redisService.opsForValue();
        operations.set(tokenKey,token,EXPIRE_TIME, TimeUnit.SECONDS);
    }

    // 删除token
    public boolean removeToken(String userAccount) {
        String tokenKey = "token:" + userAccount;
        return redisService.delete(tokenKey) != null;
    }

    // 获取token
    public String getToken(String userAccount) {
        String tokenKey = "token:" + userAccount;  // 使用相同的键格式
        ValueOperations<String, Object> operations = redisService.opsForValue();
        return (String) operations.get(tokenKey);
    }

    // 进行登录用户信息缓存
    public void UserInfoCache(String userAccount , UserVO userVO){
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userAccount;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        operations.put(userCacheKey,userKey,userVO);
        redisService.expire(userCacheKey,EXPIRE_TIME,TimeUnit.SECONDS);
    }

    // 得到登录用户缓存信息
    public UserVO getUserInfoCache(String userAccount) {
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userAccount;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        if (userAccount == null)
        {
            throw new BusinessException(StateCode.PARAMS_ERROR,"用户缓存异常");
        }
        return (UserVO) operations.get(userCacheKey, userKey);
    }

    // 删除用户缓存信息
    public void removeUserInfoCache(String userAccount) {
        String userCacheKey = "UserInfo:";
        String userKey = "UserId:" + userAccount;
        HashOperations<String, String, Object> operations = redisService.opsForHash();
        if (userAccount == null)
        {
            throw new BusinessException(StateCode.PARAMS_ERROR,"用户Id不存在");
        }
        // 从 Redis 删除 UserVO
        operations.delete(userCacheKey, userKey);
    }


}
