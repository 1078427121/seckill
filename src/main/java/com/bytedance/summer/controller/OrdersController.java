package com.bytedance.summer.controller;

import com.bytedance.summer.cheatdetect.aop.SessionCheck;
import com.bytedance.summer.cheatdetect.aop.UserLimiter;
import com.bytedance.summer.debug.CountTime;
import com.bytedance.summer.entity.*;
import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.service.OrderService;
import com.bytedance.summer.service.ProductService;
import com.bytedance.summer.service.TokenGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

/**
 * @author pjl
 * @date 2019/8/12 13:47
 * 订单相关的controller
 */
@RestController
public class OrdersController {
    @Autowired
    private OrderService orderSevice;

    @Autowired
    @Qualifier("redisTemplate0")
    private RedisTemplate redisTemplate0;
    @Autowired
    @Qualifier("redisTemplate1")
    private RedisTemplate redisTemplate1;
    @Autowired
    @Qualifier("redisTemplate2")
    private RedisTemplate redisTemplate2;

    @Autowired
    private TokenGetter tokenGetter;

    @Autowired
    private ProductService productService;

    /*
    全部订单接口
     */
//    @CountTime
    @GetMapping(value = "/result")
    public AllOrder allOrders(Integer uid, @Nullable @RequestHeader("sessionid") String sessionid) {
        try{
            return orderSevice.getAllOrder(uid);
        } catch (Exception e){
            e.printStackTrace();
            throw new HttpForbiddenException("未知错误");
        }

    }

    /*
    下单接口
     */
//    @CountTime
//    @UserLimiter
    @SessionCheck
    @PostMapping(value = "/order")
    public OrderResult order(@RequestBody OrderInput orderInput,@Nullable @RequestHeader("sessionid") String sessionid,@Nullable @RequestHeader("X-Forwarded-For") String ipAddr) {
        try {
            return orderSevice.order(orderInput);
        } catch (NumberFormatException e) {
            throw new HttpForbiddenException("非法请求参数");
        } catch (DuplicateKeyException e) {
            throw new HttpForbiddenException("您已经购买过了哦，请不要重复下单");
        } catch (Exception e){
            e.printStackTrace();
            throw new HttpForbiddenException("未知错误");
        }
    }

    /*
    支付接口
     */
//    @CountTime
//    @UserLimiter
    @SessionCheck
    @PostMapping(value = "/pay")
    public PayResult pay(@RequestBody PayInput payInput,@RequestHeader("sessionid") String sessionid,@RequestHeader("X-Forwarded-For") String ipAddr) {
        try {
            return orderSevice.pay(payInput);
        } catch (NumberFormatException e) {
            throw new HttpForbiddenException("非法请求参数");
        } catch (HttpForbiddenException e) {
            throw e;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new HttpForbiddenException("未知错误");
        }
    }
}
