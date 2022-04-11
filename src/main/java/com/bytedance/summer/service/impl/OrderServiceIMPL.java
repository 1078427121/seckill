package com.bytedance.summer.service.impl;

import com.bytedance.summer.dao.OrderMapper;
import com.bytedance.summer.entity.*;
import com.bytedance.summer.service.OrderService;
import com.bytedance.summer.service.ProductService;
import com.bytedance.summer.service.TokenGetter;
import com.bytedance.summer.tools.Constants;
import com.bytedance.summer.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * @author pjl
 * @date 2019/8/12 15:38
 * OrderService实现
 */
//@Service
public class OrderServiceIMPL implements OrderService, Constants {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productSevice;

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

    public OrderRedis getOrderRedis(PayInput payInput) {
        return null;
    }

    /*
        查询全部订单
         */
    @Override
    public AllOrder getAllOrder(Integer uid) throws DataAccessException {
        AllOrder allOrder = new AllOrder();
        allOrder.setData(orderMapper.getAllOrders());
        return allOrder;
    }

    /*
    支付
     */
    @Override
    public PayResult pay(PayInput payInput) throws DataAccessException, NumberFormatException {
        Integer uid = payInput.getUid();
        Integer price = payInput.getPrice();
        Order order = orderMapper.getOrderById(payInput.getOrder_id());
        if (order == null)
            return null;
        PayResult payResult = new PayResult();
        /*
        并发下有重复支付问题：建议将支付独立成表
        pay的校验是否可交给数据库？
        支付金额大于订单金额 是不是也可以成功？
         */
        if (order.getStatus() == 0 && price.equals(order.getPrice())) {

            String token = tokenGetter.getToken(payInput);
            orderMapper.pay(payInput.getOrder_id(), 1, token);
            payResult.setCode(PAY_SUCCESS);
            payResult.setToken(token);
        } else {
            payResult.setCode(PAY_FAILURE);
        }
        return payResult;
    }

    /*
    下单
     */
    @Override
    public OrderResult order(OrderInput orderInput) throws NumberFormatException, DuplicateKeyException {
        Integer uuid = orderInput.getUid();
        Long ppid = orderInput.getPid();
        if (redisTemplate1.opsForHash().hasKey(uuid, ppid)) {
            redisTemplate0.opsForValue().increment(ppid);
        }
        Integer uid = orderInput.getUid();
        Long pid = orderInput.getPid();

        Product product = productSevice.getProduct(pid);
        if (product == null)
            return null;
        OrderResult result = new OrderResult();
        if (redisTemplate0.opsForHash().increment("sold",pid,1) < 100) {
            Order order = new Order();
            String order_id = Tools.getOrderId(pid, uid, new Date());
            order.setOrder_id(order_id);
            order.setDetail(product.getDetail());
            order.setPid(pid);
            order.setUid(uid);
            order.setPrice(product.getPrice());
            orderMapper.insertOrder(order);
            result.setCode(ORDER_SUCCESS);
            result.setOrder_id(order_id);
        } else {
            result.setCode(ORDER_FAILURE);
        }
        return result;
    }
}
