package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.model.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.request.friends.FriendAddRequest;

/**
* @author admin
* @description 针对表【friends(好友申请管理表)】的数据库操作Service
* @createDate 2024-12-13 10:14:50
*/
public interface FriendsService extends IService<Friends> {

    /**
     * 好友申请
     *
     * @param loggingUser
     * @param friendAddRequest
     * @return
     */
    boolean addFriendRecords(UserDO loggingUser, FriendAddRequest friendAddRequest);
}
