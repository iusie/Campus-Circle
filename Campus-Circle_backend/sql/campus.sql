# 数据库初始化

# 创建库
create database if not exists campus;

# 切换库
use campus;

-- ----------------------------
-- 用户表
-- ----------------------------
drop table if exists `user`;
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    userAccount  varchar(256)                       null comment '用户账号',
    userPassword varchar(512)                       not null comment '密码',
    username     varchar(256)                       null comment '用户昵称',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    userProfile  varchar(512)                       null comment '用户自我介绍',
    gender       tinyint                            null comment '性别',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    tags         varchar(1024)                      null comment '标签 json 列表',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    userStatus   int      default 0                 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
)
    comment '用户';

-- ----------------------------
-- 用户——队伍 关系表
-- ----------------------------
drop table if exists `user_team`;
create table user_team
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint                              not null comment '用户id',
    teamId     bigint                              not null comment '队伍id',
    joinTime   datetime                            null comment '加入时间',
    createTime datetime  default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime timestamp default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete   tinyint   default 0                 not null comment '是否删除'
)
    comment '用户队伍关系';

-- ----------------------------
-- 队伍表
-- ----------------------------
drop table if exists `team`;
create table team
(
    id           bigint auto_increment comment '队伍id'
        primary key,
    userId       bigint                              not null comment '用户id',
    teamName     varchar(256)                        not null comment '队伍名称',
    description  varchar(1024)                       null comment '队伍描述',
    maxNum       int       default 1                 not null comment '最大人数',
    teamPassword varchar(512)                        null comment '队伍密码',
    teamState    int       default 0                 not null comment '状态 0-正常 1-私有  2-加密',
    expireTime   datetime                            null comment '过期时间',
    createTime   datetime  default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   timestamp default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete     tinyint   default 0                 not null comment '是否删除'
)
    comment '队伍';

-- ----------------------------
-- 文章表
-- ----------------------------
drop table if exists `article`;
create table article
(
    id         bigint auto_increment comment '文章id'
        primary key,
    userId     bigint                              not null comment '用户id',
    headline   varchar(1024)                       not null comment '文章标题',
    content    text                                not null comment '文章内容',
    summarized varchar(1024)                       null comment '文章概括',
    coverUrl   varchar(1024)                       null comment '文章封面',
    articleState  int       default 0              not null comment '状态 0 正常',
    articleTags varchar(1024)                      null comment '标签 json 列表',
    thumbNum   int       default 0                 not null comment '点赞数',
    createTime datetime  default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint   default 0                 not null comment '是否删除'
)
    comment '文章';

-- ----------------------------
-- 文章点赞
-- ----------------------------
drop table if exists `article_thumb`;
create table article_thumb
(
    id         bigint auto_increment comment 'id'
        primary key,
    articleId     bigint                             not null comment '文章 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_articleId(articleId),
    index idx_userId(userId)
)
    comment '文章点赞';

-- ----------------------------
-- 文章评论
-- ----------------------------
drop table if exists `article_comment`;
create table article_comment
(
    id           bigint auto_increment comment '文章评论id'
        primary key,
    userId       bigint                              not null comment '评论用户id',
    articleId    bigint                              not null comment '评论帖子id',
    content      varchar(256)                        not null comment '评论内容(最大200字)',
    pid          bigint                              not null comment '父id',
    commentState int       default 0                 not null comment '状态 0 正常',
    createTime   datetime  default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   timestamp default CURRENT_TIMESTAMP not null comment '更新时间',
    isDelete     tinyint   default 0                 not null comment '是否删除'
)
    comment '文章评论';


-- ----------------------------
-- 用户关注表
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`
(
    `id`             bigint(20)          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`        bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
    `follow_user_id` bigint(20) UNSIGNED NOT NULL COMMENT '关注的用户id',
    `create_time`    timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`      tinyint(4)          NULL     DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) comment '用户关注' charset = utf8;

-- ----------------------------
-- 好友申请表
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '好友申请id',
    `from_id`     bigint(20)                                                    NOT NULL COMMENT '发送申请的用户id',
    `receive_id`  bigint(20)                                                    NULL     DEFAULT NULL COMMENT '接收申请的用户id ',
    `is_read`     tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '是否已读(0-未读 1-已读)',
    `status`      tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-不同意）',
    `create_time` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP,
    `is_delete`   tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除',
    `remark`      varchar(214)                                                  NULL     DEFAULT NULL COMMENT '好友申请备注信息',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '好友申请管理表';


-- ----------------------------
-- 消息表
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`        tinyint(4)                                                    NULL DEFAULT NULL COMMENT '类型-1 点赞',
    `from_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '消息发送的用户id',
    `to_id`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '消息接收的用户id',
    `data`        varchar(255)                                                  NULL DEFAULT NULL COMMENT '消息内容',
    `is_read`     tinyint(4)                                                    NULL DEFAULT 0 COMMENT '已读-0 未读 ,1 已读',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(4)                                                    NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) comment '消息表' charset = utf8;


-- ----------------------------
-- 聊天信息表
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`
(
    `id`          bigint(20) PRIMARY KEY                                        NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
    `from_id`     bigint(20)                                                    NOT NULL COMMENT '发送消息的群/用户id',
    `to_id`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收消息的群/用户id',
    `text`        varchar(512)                                                  NULL DEFAULT NULL,
    `chat_type`   tinyint(4)                                                    NOT NULL COMMENT '聊天类型 1-私聊 2-群聊',
    `is_read`     tinyint                                                            default 0 null comment '是否已读 1-已读 2-未读',
    `create_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `team_id`     bigint(20)                                                    NULL DEFAULT NULL,
    `is_delete`   tinyint(4)                                                    NULL DEFAULT 0
)
    COMMENT = '聊天消息表';