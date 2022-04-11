package com.bytedance.summer.entity;

/**
 * @author pjl
 * @date 2019/8/12 15:47
 * 下订单返回结果
 */
public class OrderResult {
    private Integer code;
    private String order_id;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getCode() {
        return code;
    }

    public String getOrder_id() {
        return order_id;
    }
}
