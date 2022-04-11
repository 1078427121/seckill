package com.bytedance.summer.debug;

import com.bytedance.summer.exception.HttpForbiddenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author pjl
 * @date 2019/8/29 3:23
 */
@Component
@Scope
@Aspect
public class CountTimeAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.bytedance.summer.debug.CountTime)")
    public void countTime() {
    }

    @Around("countTime()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ans = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        long a=System.nanoTime();
        try{
            ans = joinPoint.proceed();
        }finally {
            logger.info(msig.getName()+""+(a-System.nanoTime()));
        }
        return ans;
    }
}
