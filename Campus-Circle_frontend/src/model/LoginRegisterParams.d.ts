export interface UserLoginParams {
  /** 用户账号 */
  userAccount?: string;

  /** 用户密码 */
  userPassword?: string;
}

export interface UserRegisterParams {
  /** 用户账号 */
  userAccount?: string;

  /** 用户密码 */
  userPassword?: string;

  /** 确认密码 */
  checkPassword?: string;

  /** 用户邮箱 */
  email?: string;
}
