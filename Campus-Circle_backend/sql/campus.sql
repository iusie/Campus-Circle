# 数据库初始化

-- 创建库
create database if not exists campus;

-- 切换库
use campus;

-- 用户表
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

-- 用户——队伍 关系表
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


-- 队伍表
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