package com.iusie.campuscircle.aop;

import com.iusie.campuscircle.annotation.OperationLogger;
import com.iusie.campuscircle.model.dto.UserDO;
import com.iusie.campuscircle.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static com.iusie.campuscircle.constant.UserConstant.*;

/**
 * 请求响应日志 AOP
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Resource
    UserService userService;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(operationLogger)")
    public void logPointcut(OperationLogger operationLogger) {
    }

    /**
     * 执行拦截
     */
    @Around(value = "logPointcut(operationLogger)", argNames = "point,operationLogger")
    public Object doInterceptor(ProceedingJoinPoint point, OperationLogger operationLogger) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //用户id
        UserDO user = userService.getLoggingUser(httpServletRequest);
        String userAccount = user.getUserAccount();
        int userRole = user.getUserRole();
        String role = userRole(userRole);

        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        String description = operationLogger.value();
        //String ipSource = IPUtils.getCityInfo(ip);
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        // 输出请求日志
        log.info("[日志编号: {} 用户权限: {} 用户账户: {} 请求路径: {} 请求方法: {} 执行任务：[{}] 执行操作: {} 用时: {}ms]"
                , requestId, role, userAccount, url, method, description, reqParam, totalTimeMillis);
        return result;
    }

    private String userRole(int userRole) {
        if (userRole == DEFAULT_ROLE) {
            return DEFAULT;
        } else if (userRole == ADMIN_ROLE) {
            return ADMIN;
        } else {
            return BAN;
        }

    }

}

