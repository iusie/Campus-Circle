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
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
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