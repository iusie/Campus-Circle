package com.iusie.campuscircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.manager.RedisService;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Friends;
import com.iusie.campuscircle.model.request.friends.FriendAddRequest;
import com.iusie.campuscircle.service.FriendsService;
import com.iusie.campuscircle.mapper.FriendsMapper;
import com.iusie.campuscircle.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.iusie.campuscircle.constant.FriendConstant.DEFAULT_STATUS;
import static com.iusie.campuscircle.constant.RedissonConstant.ADD_FRIENDS_LOCK;

/**
 * @author admin
 * @description 针对表【friends(好友申请管理表)】的数据库操作Service实现
 * @createDate 2024-12-13 10:14:50
 */
@Slf4j
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends>
        implements FriendsService {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonService;

    @Resource
    private RedisService redisService;

    /**
     * 好友申请
     *
     * @param loggingUser
     * @param friendAddRequest
     * @return
     */
    @Override
    public boolean addFriendRecords(UserDO loggingUser, FriendAddRequest friendAddRequest) {
        if (StringUtils.isNotBlank(friendAddRequest.getRemark()) && friendAddRequest.getRemark().length() > 120) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "验证消息过长");
        }
        if (ObjectUtils.anyNull(loggingUser.getId(), friendAddRequest.getReceiveId())) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "添加失败");
        }
        // 1.不能添加自己为好友
        if (Objects.equals(loggingUser.getId(), friendAddRequest.getReceiveId())) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "不能添加自己为好友");
        }
        RLock lock = redissonService.getLock(ADD_FRIENDS_LOCK + loggingUser.getId());
        try {
            // 强锁执行
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                // 2.条数大于等于1 就不能再添加
                LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
                friendsLambdaQueryWrapper.eq(Friends::getReceiveId, friendAddRequest.getReceiveId());
                friendsLambdaQueryWrapper.eq(Friends::getFromId, loggingUser.getId());
                List<Friends> list = this.list(friendsLambdaQueryWrapper);
                list.forEach(friends -> {
                    if (list.size() > 1 && friends.getStatus() == DEFAULT_STATUS) {
                        throw new BusinessException(StateCode.PARAMS_ERROR, "不能重复申请");
                    }
                });
                // 将发送申请者的信息保存
                Friends applySendFriend = new Friends();
                applySendFriend.setFromId(loggingUser.getId());
                applySendFriend.setReceiveId(friendAddRequest.getReceiveId());

                if (StringUtils.isBlank(friendAddRequest.getRemark())) {
                    applySendFriend.setRemark("我是" + userService.getById(loggingUser.getId()).getUsername());
                } else {
                    applySendFriend.setRemark(friendAddRequest.getRemark());
                }

                // 被申请者消息保存于记录中
                Friends receiveFriend = new Friends();
                receiveFriend.setFromId(friendAddRequest.getReceiveId());
                receiveFriend.setReceiveId(loggingUser.getId());
                this.save(receiveFriend);

                return this.save(applySendFriend);
            }
        } catch (InterruptedException e) {
            log.error("添加好友错误", e);
            return false;
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                log.info("添加好友释放的线程ID: {}", "unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
        return false;
    }
}




