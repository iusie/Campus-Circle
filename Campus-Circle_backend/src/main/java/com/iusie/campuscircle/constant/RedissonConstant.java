package com.iusie.campuscircle.constant;

/**
 * @author iusie
 * @description redisson常量
 * @date 2024/12/13
 */
public interface RedissonConstant {
    /**
     * 应用锁
     */
    String ADD_FRIENDS_LOCK = "addFriends:lock:";

    String JOIN_TEAM_KEY = "joinTeam:lock";
}
