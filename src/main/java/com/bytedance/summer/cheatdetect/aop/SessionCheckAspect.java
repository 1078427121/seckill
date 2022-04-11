package com.bytedance.summer.cheatdetect.aop;

import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.ratelimit.aop.RateLimitBasic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pjl
 * @date 2019/8/19 11:53
 */
@Component
@Scope
@Aspect
public class SessionCheckAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

/*
基于redis的实现方式
 */
//    @Autowired
//    private RedisTemplate redisTemplate0;
//
//    private String redisKey="uid_sessionkey";

    @Value("${uidsessionidfile}")
    private String cacheFileName;

    static private Map<Integer,String> uidSessionIdMap;

    @PostConstruct
    private void initCache(){
        uidSessionIdMap=new HashMap<>();
        try{
            BufferedReader br=new BufferedReader(new FileReader(cacheFileName));
            String line=br.readLine();
            while(line!=null){
                String[] elements=line.split("\t");
                uidSessionIdMap.put(Integer.valueOf(elements[0]),elements[1]);
                line=br.readLine();
            }
        }catch (IOException e){

        }

    }

    @Pointcut("@annotation(com.bytedance.summer.cheatdetect.aop.SessionCheck)")
    public void sessionCheck() {
    }

    @Around("sessionCheck()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ans=null;
        /*这里是存在隐患的。因为controller中参数的位置0/1不一定是uid和sessionid。
        可以采取先获取参数值数组，再获取参数名字数组，通过搜索名字找到对应位置再进行获取，
        但可能效率会低。。。*/
        Object data = Arrays.asList(joinPoint.getArgs()).get(0);
        Method getUidMethod = data.getClass().getDeclaredMethod("getUid");
        Integer uid= (Integer) getUidMethod.invoke(data);
        String sessionid = (String)  Arrays.asList(joinPoint.getArgs()).get(1);
        /*
        基于redis的实现方式
         */
//        String sessionidTrue=(String)redisTemplate0.opsForHash().get(redisKey,uid);
        String sessionidTrue=uidSessionIdMap.get(uid);
        if(!sessionid.equals(sessionidTrue)){
            throw new HttpForbiddenException("用户信息错误");
        }else{
            ans =joinPoint.proceed();
        }
        return ans;
    }
}
