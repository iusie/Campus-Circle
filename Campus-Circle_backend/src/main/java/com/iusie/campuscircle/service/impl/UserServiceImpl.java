package com.iusie.campuscircle.service.impl;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iusie.campuscircle.common.StateCode;
import com.iusie.campuscircle.exception.BusinessException;
import com.iusie.campuscircle.manager.RedisService;
import com.iusie.campuscircle.model.entity.User;
import com.iusie.campuscircle.mapper.UserMapper;
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
        redisService.UserInfoCache(userId, userVO);
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


}




