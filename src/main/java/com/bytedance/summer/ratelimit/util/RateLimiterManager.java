package com.bytedance.summer.ratelimit.util;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pjl
 * @date 2019/8/4 11:48
 * RateLimiter管理器，用于为不同接口分别限流
 */
@Component
public class RateLimiterManager {
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    public  RateLimiter getRateLimiter(Integer limit,String methodName){
        if(!map.containsKey(methodName)){
            map.put(methodName,RateLimiter.create(limit));
        }
        return map.get(methodName);
    }
}
