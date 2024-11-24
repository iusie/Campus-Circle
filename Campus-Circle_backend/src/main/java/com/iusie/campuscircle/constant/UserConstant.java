package com.iusie.campuscircle.constant;

/**
 * 用户常量
 *
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "online";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion

    /**
     * secretKey 密钥
     */
    String SECRET_KEY="qxT5SRJh8QQoHwbwhyyTvf+OVSfD/tWjG3Mbo9xiR80=";

    /**
     * tonken 过期时间
     */
    long EXPIRE_TIME=7200000;

}
