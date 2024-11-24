package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.request.UserRegisterRequest;
import com.iusie.campuscircle.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
* @author iusie
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-11-23 12:15:17
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册信息实体
     * @return 返回用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @param response 请求对象
     * @return 用户信息
     */
    UserVO userLogin(String userAccount, String userPassword, HttpServletResponse response);

    /**
     * 用户注销
     *
     * @param request servlet对象
     * @return 1为退出成功
     */
    int userLogout(HttpServletRequest request);
}
