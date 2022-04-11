package com.bytedance.summer.ratelimit.aop;

import com.bytedance.summer.ratelimit.util.TokenBucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author pjl
 * @date 2019/8/4 15:17
 * 令牌桶算法的切面
 * 反正来一个请求就尝试获取令牌，幸运儿就会拿到，不幸的就被拒绝了
 *
 */
@Component
@Scope
@Aspect
public class RateLimitTokenBucketAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private TokenBucket tokenBucket=new TokenBucket(5 ,10);
    @Autowired
    private HttpServletResponse response;

    @Pointcut("@annotation(com.bytedance.summer.ratelimit.aop.RateLimitTokenBucket)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            //获取到token则执行逻辑，否则返回403
            if (tokenBucket.getToken()) {
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
