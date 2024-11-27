package com.iusie.campuscircle.constant;

/**
 * 用户常量
 *
 */
public interface UserConstant {

    //  region 权限

    /**
     * 默认角色
     */
    int DEFAULT_ROLE = 0;

    String DEFAULT = "普通用户";

    /**
     * 管理员角色
     */
    int ADMIN_ROLE = 1;

    String ADMIN = "管理员";

    /**
     * 被封号
     */
    int BAN_ROLE = 2;

    String BAN = "黑名单用户";

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
