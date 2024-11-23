package com.iusie.campuscircle.service.impl;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.mapper.UserMapper;
import com.iusie.campuscircle.model.vo.UserRegisterRequest;
import com.iusie.campuscircle.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author iusie
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-11-23 12:15:17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    private static final String SALT = "iusie";

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userName = userRegisterRequest.getUsername();
        String Phone = userRegisterRequest.getPhone();
        String Email = userRegisterRequest.getEmail();
        int Gender = userRegisterRequest.getGender();
        //账号，密码 不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号或密码为空");
        }
        // 账号校验
        if (userAccount.length() < 4) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号长度小于4");
        }
        // 密码校验
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "密码长度小于8");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "校验密码不一致");
        }
        //账号非法字符校验
        String validPattern = "[\\u4e00-\\u9fa5\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号含非法字符");
        }
        if (userName.length() > 20) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户名过长");
        }
        if (StringUtils.isNotBlank(Phone) && !Validator.isMobile(Phone)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "手机号格式错误");
        }
        if (StringUtils.isNotBlank(Email) && !Validator.isMobile(Email)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "邮箱号格式错误");
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号重复");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUsername(userName);
        user.setGender(Gender);
        user.setPhone(Phone);
        user.setEmail(Email);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(StateCode.SYSTEM_ERROR, "数据插入错误");
        }
        return user.getId();
    }


}




