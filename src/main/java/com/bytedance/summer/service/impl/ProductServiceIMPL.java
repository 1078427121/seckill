package com.bytedance.summer.service.impl;

import com.bytedance.summer.dao.ProductMapper;
import com.bytedance.summer.entity.Product;
import com.bytedance.summer.entity.ProductMysql;
import com.bytedance.summer.service.ProductService;
import com.bytedance.summer.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author pjl
 * @date 2019/8/12 15:38
 * ProductService实现
 */
@Service
public class ProductServiceIMPL implements ProductService, Constants {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate redisTemplate0;

    /*
    查询商品
     */
    @Override
    public Product getProduct(Long pid) throws DataAccessException, NumberFormatException {
        int count = getCountRedis(pid);
        ProductMysql productMysql = getProductMysql(pid);
        Product product = new Product();
        product.setCount(count);
        product.setDetail(productMysql.getDetail());
        product.setPid(pid);
        product.setPrice(productMysql.getPrice());
        return product;
    }

    @Cacheable(value = "countCache", key = "#pid")
    private int getCountRedis(Long pid) {
         Integer soldCount =(Integer) redisTemplate0.opsForValue().get(pid);
        if (soldCount == null) {
            soldCount = 0;
        }
        return MAX_PRODUCT_SIZE - soldCount;
    }

    @Cacheable(value = "productCache", key = "#pid")
    private ProductMysql getProductMysql(Long pid) {
//        System.out.println("product读了数据库");
        return productMapper.getProduct(pid);
    }
}
