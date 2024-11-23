package com.iusie.campuscircle.service;

import com.iusie.campuscircle.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iusie.campuscircle.model.vo.UserRegisterRequest;

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
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);
}
