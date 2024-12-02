export interface UserLoginRes {
  /**
   * 用户id
   */
    id: number;

  /**
   * 用户账号
   */
    userAccount: string;

  /**
   * 用户昵称
   */
    username: string;

  /**
   * 用户头像
   */
    avatarUrl: string;

  /**
   * 用户自我介绍
   */
    userProfile: string;

  /**
   * 性别
   */
    gender: number;

  /**
   * 电话
   */
    phone: string;

  /**
   * 邮箱
   */
    email: string;

  /**
   * 标签 json 列表
   */
    tags: Record<string, unknown>[];

  /**
   * 用户角色 0 - 普通用户 1 - 管理员
   */
    userRole: number;

  /**
   * 状态 0 - 正常
   */
    userStatus: number;

  /**
   * 创建时间
   */
    createTime: Record<string, unknown>;
  }
