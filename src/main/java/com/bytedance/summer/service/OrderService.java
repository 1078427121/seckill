package com.bytedance.summer.service;

import com.bytedance.summer.entity.*;

/**
 * @author pjl
 * @date 2019/8/12 15:41
 * Order 相关服务接口
 */
public interface OrderService {
    AllOrder getAllOrder(Integer uid);
    PayResult pay(PayInput payInput);
    OrderResult order(OrderInput orderInput);
}
