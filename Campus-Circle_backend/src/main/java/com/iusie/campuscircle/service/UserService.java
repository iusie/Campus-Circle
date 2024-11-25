package com.iusie.campuscircle.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.iusie.campuscircle.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.request.UpdateUserRequest;
import com.iusie.campuscircle.model.request.UserRegisterRequest;
import com.iusie.campuscircle.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

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


    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return
     */
    User getLoggingUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
    boolean isAdmin(User user);

    /**
     * 更新用户信息(同时更新缓存)
     * @param updateUserRequest 前端返回的数据
     * @param request
     * @return 1为更新成功，0为失败
     */
    boolean updateUser(UpdateUserRequest updateUserRequest, HttpServletRequest request);

    /**
     * 查看用户信息
     *
     * @param queryById id实体
     * @param loggingUser 返回的用户数据
     * @return User
     */
    User getUserInfoById(Long queryById, User loggingUser);

    /**
     * 用户搜索
     *
     * @param userAccount 搜索实体
     * @param loggingUser 返回的用户数据
     * @return List<User>
     */
    List<UserVO> searchUsers(String userAccount , String userName,  User loggingUser);

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @return Wrapper<User>
     */
    Wrapper<User> getQueryWrapper();
}
