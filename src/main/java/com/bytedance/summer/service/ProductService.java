package com.bytedance.summer.service;

import com.bytedance.summer.entity.Product;

/**
 * @author pjl
 * @date 2019/8/12 15:41
 * Product 相关服务接口
 */
public interface ProductService {
    Product getProduct(Long pid);
}
