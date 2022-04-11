package com.bytedance.summer.controller;

import com.bytedance.summer.cheatdetect.aop.UserLimiter;
import com.bytedance.summer.debug.CountTime;
import com.bytedance.summer.entity.Product;
import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

/**
 * @author pjl
 * @date 2019/8/12 13:47
 * 商品相关的controller
 */
@RestController
public class ProductsController {
    @Autowired
    private ProductService productSevice;
    /*
    商品信息接口
     */
//    @CountTime
//    @UserLimiter
    @GetMapping("/product")
    public Product allOrders(Long pid, @Nullable @RequestHeader("sessionid") String sessionid,  @Nullable @RequestHeader("X-Forwarded-For") String ipAddr) {
        try {
            return productSevice.getProduct(pid);
        } catch (NumberFormatException e) {
            throw new HttpForbiddenException("非法请求参数");
        } catch (Exception e){
            e.printStackTrace();
            throw new HttpForbiddenException("未知错误");
        }
    }
}
