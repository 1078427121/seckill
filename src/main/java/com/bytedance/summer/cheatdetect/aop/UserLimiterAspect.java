package com.bytedance.summer.cheatdetect.aop;

import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.ratelimit.aop.RateLimitBasic;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author pjl
 * @date 2019/8/19 11:53
 * 由于只能对redis顶层key设置过期时间
 * 所以不能对每个接口分别限制其使用频率。
 * 不能设置比如读取较宽松写入较严格的策略。
 * 可以改进吗？
 */
@Component
@Scope
@Aspect
public class UserLimiterAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StringRedisTemplate redisTemplate3;

    @Pointcut("@annotation(com.bytedance.summer.cheatdetect.aop.UserLimiter)")
    public void userLimit() {
    }

    @Around("userLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ans=null;
        /*这里是存在隐患的。因为controller中参数的位置1/2不一定是sessionid和ipAddr。
        可以采取先获取参数值数组，再获取参数名字数组，通过搜索名字找到对应位置再进行获取，
        但可能效率会低。。。*/
        String sessionid = (String)  Arrays.asList(joinPoint.getArgs()).get(1);
        String ipAddr = (String)  Arrays.asList(joinPoint.getArgs()).get(2);
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        UserLimiter annotation = currentMethod.getAnnotation(UserLimiter.class);
        int maxCount = annotation.maxCount();
        int seconds = annotation.seconds();
        String str1=redisTemplate3.opsForValue().get(sessionid);
        int count1 = Integer.parseInt(str1 == null ? "0" : str1);
        String str2=redisTemplate3.opsForValue().get(ipAddr);
        int count2 = Integer.parseInt(str2 == null ? "0" : str2);
        if(count1 >maxCount|| count2 >maxCount){
            logger.info(RateLimitBasic.class.getSimpleName() + ":访问过频，请稍后再试");
            throw new HttpForbiddenException("访问过频，请稍后再试");
        }else{
            redisTemplate3.opsForValue().increment(sessionid,1);
            redisTemplate3.expire(sessionid, seconds, TimeUnit.SECONDS);
            redisTemplate3.opsForValue().increment(ipAddr,1);
            redisTemplate3.expire(ipAddr, seconds, TimeUnit.SECONDS);
            ans =joinPoint.proceed();
        }
        return ans;
    }
}
