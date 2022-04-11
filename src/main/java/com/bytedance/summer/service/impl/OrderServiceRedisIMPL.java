package com.bytedance.summer.service.impl;

import com.bytedance.summer.dao.OrderMapper;
import com.bytedance.summer.entity.*;
import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.service.OrderService;
import com.bytedance.summer.service.ProductService;
import com.bytedance.summer.service.TokenGetter;
import com.bytedance.summer.tools.Constants;
import com.bytedance.summer.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceRedisIMPL implements OrderService, Constants {
    String redisHashNameOrder = "order";

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenGetter tokenGetter;

    @Autowired
    @Qualifier("redisTemplate0")
    private RedisTemplate redisTemplate0;
    @Autowired
    @Qualifier("redisTemplate1")
    private RedisTemplate redisTemplate1;
    @Autowired
    @Qualifier("redisTemplate2")
    private RedisTemplate redisTemplate2;

    private OrderRedis getOrderRedis(Integer uuid, Long ppid) {
        return (OrderRedis) redisTemplate1.opsForHash().get(uuid, ppid);
    }


    /*
        查询全部订单
         */
    @Override
    public AllOrder getAllOrder(Integer uid) throws DataAccessException {
        AllOrder allOrder = new AllOrder();
        Map<Long, OrderRedis> redisOrder = redisTemplate1.opsForHash().entries(uid);
        Map<Long, String> redisToken = redisTemplate2.opsForHash().entries(uid);
        Queue<Order> ans = new PriorityQueue<>((o1, o2) -> (int) (o1.getPid() - o2.getPid()));
        redisOrder.forEach((pid, orderRedis) -> {
            Order o = new Order();
            o.setPid(pid);
            o.setDetail(orderRedis.getDetail());
            o.setPrice(orderRedis.getPrice());
            o.setOrder_id(orderRedis.getOrder_id());
            o.setUid(uid);
            String token = redisToken.get(pid);
            o.setStatus(token == null ? 0 : 1);
            o.setToken(token == null ? "" : token);
            ans.add(o);
        });
        allOrder.setData(ans);
        return allOrder;
    }

    /*
    支付
     */
    @Override
    public PayResult pay(PayInput payInput) throws NumberFormatException {
        Integer uuid = payInput.getUid();
        Long ppid = Tools.getPidFromOrderId(payInput.getOrder_id());
        OrderRedis orderRedis = getOrderRedis(uuid, ppid);
        //有订单并且支付金额没毛病
        if (orderRedis != null) {
            if (payInput.getPrice().equals(orderRedis.getPrice())) {
                PayResult payResult = new PayResult();
                payResult.setCode(PAY_FAILURE);
                //查询订单是否已支付（此设计假设tokenserver比redis慢很多，否则可以直接拿token去尝试支付）
                if (!redisTemplate2.opsForHash().hasKey(uuid, ppid)) {
                    String token = tokenGetter.getToken(payInput);
                    //尝试插入token
                    if (redisTemplate2.opsForHash().putIfAbsent(uuid, ppid, token)) {
                        payResult.setCode(PAY_SUCCESS);
                        payResult.setToken(token);
                    }

                }
                return payResult;
            } else {
                throw new HttpForbiddenException("支付金额异常");
            }

        } else {
            throw new HttpForbiddenException("没有查到订单");
        }

    }

    /*
    下单
     */
    @Override
    public OrderResult order(OrderInput orderInput) throws NumberFormatException {
        Integer uuid = orderInput.getUid();
        Long ppid = orderInput.getPid();
        OrderResult result = new OrderResult();
        result.setCode(ORDER_FAILURE);
        //判断重复下单
        if (!redisTemplate1.opsForHash().hasKey(uuid, ppid)) {

            Long soldCount = redisTemplate0.opsForValue().increment(ppid);
            /*
            高并发下重复下单用户可能导致商品少卖！！！！！
             */
            //判断是否还有剩余
            if (soldCount < 100) {
                OrderRedis orderRedis = new OrderRedis();
                String order_id = Tools.getOrderId(ppid, uuid, new Date());
                orderRedis.setOrder_id(order_id);
                orderRedis.setDetail(productService.getProduct(ppid).getDetail());
                orderRedis.setPrice(productService.getProduct(ppid).getPrice());
                //尝试插入订单
                if (redisTemplate1.opsForHash().putIfAbsent(uuid, ppid, orderRedis)) {
                    result.setCode(ORDER_SUCCESS);
                    result.setOrder_id(order_id);
                } else {
                    //回滚
                    soldCount = redisTemplate0.opsForValue().decrement(ppid);
                    //解决部分少卖问题，但此判定通过后仍然存在少卖
                    if (soldCount >= MAX_PRODUCT_SIZE) {
                        redisTemplate0.opsForValue().set(ppid, MAX_PRODUCT_SIZE - 1);
                    }
                }
            }
        }
        return result;
    }
}
