package com.iusie.campuscircle.service.impl;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.manager.RedisService;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.mapper.UserMapper;
import com.iusie.campuscircle.model.request.UpdateUserRequest;
import com.iusie.campuscircle.model.request.UserRegisterRequest;
import com.iusie.campuscircle.model.vo.UserVO;
import com.iusie.campuscircle.service.UserService;
import com.iusie.campuscircle.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.iusie.campuscircle.constant.UserConstant.ADMIN_ROLE;

/**
 * @author iusie
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-11-23 12:15:17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;


    private static final String SALT = "iusie";

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册信息实体
     * @return 返回用户id
     */
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

    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param response     请求对象
     * @return 用户信息
     */
    @Override
    public UserVO userLogin(String userAccount, String userPassword, HttpServletResponse response) {
        //账号，密码 不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "登录参数为空");
        }
        // 账号校验
        if (userAccount.length() < 4) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号长度小于4");
        }
        //账号非法字符校验
        String validPattern = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号非法");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //账号查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            throw new BusinessException(StateCode.PARAMS_ERROR, "账号或者密码错误");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        Long userId = userVO.getId();
        // 把用户信息写入Redis
        redisService.UserInfoCache(userId, user);
        // 生成Token保存到Redis中
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        // 生成token
        String token = JwtUtils.genToken(claims);
        // 把token保存到redis中
        redisService.saveToken(userId, token);
        // 将 Token 写入响应头
        response.setHeader("Authorization", token);
        return userVO;
    }

    /**
     * 用户注销
     *
     * @param request servlet对象
     * @return 1为退出成功
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "Token状态异常");
        }
        Map<String, Object> parsed = JwtUtils.parseToken(token);
        Long userId = (Long) parsed.get("userId");
        redisService.removeUserInfoCache(userId);
        redisService.removeToken(userId);
        return 1;
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request servlet对象
     * @return UserVO
     */
    @Override
    public User getLoggingUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Map<String, Object> parsed = JwtUtils.parseToken(token);
        Long userId = (Long) parsed.get("userId");
        User cache = redisService.getUserInfoCache(userId);
        if (cache != null) {
            return cache;
        }
        // 如果缓存不存在，读数据库
        // todo 读数据库除了Redis异常，还有可能是账号异常
        log.info("账号存在异常,请尽快修改密码");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectById(queryWrapper);
    }

    /**
     * 查询是否为管理员
     *
     * @param request 请求头封装信息
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            Map<String, Object> parsed = JwtUtils.parseToken(token);
            Long userId = (Long) parsed.get("userId");
            User cache = redisService.getUserInfoCache(userId);
            return cache.getUserRole() == ADMIN_ROLE;
        } catch (Exception e) {
            throw new BusinessException(StateCode.NOT_FOUND_ERROR, "Redis或账号异常");
        }
    }

    @Override
    public boolean isAdmin(User loginUser) {
        if (loginUser == null) {
            return false;
        }
        return loginUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 更新用户信息(同时更新缓存)
     *
     * @param updateUserRequest 返回的数据
     * @param request
     * @return 1为更新成功，0为失败
     */
    @Override
    public boolean updateUser(UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        long userId = updateUserRequest.getId();
        User loginUser = this.getLoggingUser(request);
        if (userId <= 0) {
            throw new BusinessException(StateCode.PARAMS_ERROR);
        }
        //如果是管理员可以改任何人的信息
        //本人可以改自己的信息
        if (!isAdmin(loginUser) && loginUser.getId() != userId) {
            throw new BusinessException(StateCode.NO_AUTH_ERROR, "无权限");
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "参数为空");
        }
        //如果更新密码,非管理员需要校验两次密码是否相等
        String newPassword = DigestUtils.md5DigestAsHex((SALT + updateUserRequest.getUserPassword()).getBytes());
        String surePassword = DigestUtils.md5DigestAsHex((SALT + updateUserRequest.getSurePassword()).getBytes());
        if (!oldUser.getUserPassword().equals(newPassword) && !isAdmin(loginUser) && !newPassword.equals(surePassword)) {
            throw new BusinessException(StateCode.OPERATION_ERROR, "修改的两次密码不一致");
        }
        if (!oldUser.getUserPassword().equals(newPassword)) {
            redisService.removeToken(userId);
        }
        updateUserRequest.setUserPassword(newPassword);
        User user = new User();
        BeanUtils.copyProperties(updateUserRequest, user);
        //0为男，1为女
        Integer gender = user.getGender();
        if (gender != null) {
            if (gender != 1 && gender != 0) {
                return false;
            }
        }
        if (user.getUsername().length() > 20) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "用户名过长");
        }
        String Phone = user.getPhone();
        if (StringUtils.isNotBlank(Phone) && !Validator.isMobile(Phone)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "手机号格式错误");
        }
        String Email = user.getEmail();
        if (StringUtils.isNotBlank(Email) && !Validator.isMobile(Email)) {
            throw new BusinessException(StateCode.PARAMS_ERROR, "邮箱号格式错误");
        }
        //过滤普通用户不能更改的字段
        if (!isAdmin(loginUser)) {
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setIsDelete(0);
        }
        int updateById = userMapper.updateById(user);
        if (updateById != 1) {
            return false;
        }
        //更新缓存信息
        redisService.UserInfoCache(userId, user);

        return true;
    }


}




