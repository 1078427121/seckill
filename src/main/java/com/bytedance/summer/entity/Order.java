package com.bytedance.summer.entity;

import java.io.Serializable;

/**
 * @author pjl
 * @date 2019/8/12 14:21
 * 订单信息
 * 返回所有订单也适用
 */
public class Order implements Serializable {
    private String order_id;
    private Long pid;
    private Integer uid;
    private Integer price;
    private String detail;
    private Integer status;
    private String token;

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Long getPid() {
        return pid;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public Integer getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
