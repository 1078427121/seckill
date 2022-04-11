package com.bytedance.summer.entity;

public class OrderRedis {
    private String order_id;
    private Integer price;
    private String detail;

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOrder_id() {
        return order_id;
    }


    public Integer getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }
}
