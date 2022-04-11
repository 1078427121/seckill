package com.bytedance.summer.entity;

/**
 * @author pjl
 * @date 2019/8/16 19:01
 */
public class PayInput {
    private Integer uid;
    private Integer price;
    private String order_id;

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getPrice() {
        return price;
    }

    public String getOrder_id() {
        return order_id;
    }
}
