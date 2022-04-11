package com.bytedance.summer.ratelimit.aop;


import com.bytedance.summer.ratelimit.util.RateLimiterManager;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author pjl
 * @date 2019/8/4 11:32
 * 基于RateLimiter的切面
 */
@Component
@Scope
@Aspect
public class RateLimitBasicAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private RateLimiter rateLimiter;
    @Autowired
    private RateLimiterManager rateLimiterManager;
    @Autowired
    private HttpServletResponse response;

    @Pointcut("@annotation(com.bytedance.summer.ratelimit.aop.RateLimitBasic)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimitBasic annotation = currentMethod.getAnnotation(RateLimitBasic.class);
        int limitNum = annotation.limitNum();
        rateLimiter = rateLimiterManager.getRateLimiter(limitNum, RateLimitBasic.class.getName());
        try {
            //获取到token则执行逻辑，否则返回403
            if (rateLimiter.tryAcquire()) {
                obj = joinPoint.proceed();
            } else {
                String result = "系统忙，拒绝访问";
                log.info(RateLimitBasic.class.getSimpleName() + ":" + result);
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(403);
                try (ServletOutputStream outputStream = response.getOutputStream()) {
                    outputStream.write(result.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
