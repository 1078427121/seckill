package com.bytedance.summer.service.impl;

import com.bytedance.summer.entity.ResetResult;
import com.bytedance.summer.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResetServiceIMPL implements ResetService {
    @Autowired
    @Qualifier("redisTemplate0")
    private RedisTemplate redisTemplate0;
    @Autowired
    @Qualifier("redisTemplate1")
    private RedisTemplate redisTemplate1;
    @Autowired
    @Qualifier("redisTemplate2")
    private RedisTemplate redisTemplate2;


    @Value("${token}")
    String token;

    @Override
    public ResetResult reset(String token) {
        ResetResult ans = new ResetResult();
        if (token.equals(this.token)) {
            redisTemplate0.execute((RedisCallback<Boolean>) connection -> {
                connection.flushAll();
                return true;
            });
            redisTemplate1.execute((RedisCallback<Boolean>) connection -> {
                connection.flushAll();
                return true;
            });
            redisTemplate2.execute((RedisCallback<Boolean>) connection -> {
                connection.flushAll();
                return true;
            });
//            clearCache();
            ans.setCode(0);
        } else {
            ans.setCode(1);
        }
        return ans;
    }

    /*
    不知道为啥反正没清掉缓存，干脆不用了
     */
//    @CacheEvict(value="productCache",allEntries=true)
//    private void clearCache(){
//        System.out.println("缓存清掉了");
//    }
}
