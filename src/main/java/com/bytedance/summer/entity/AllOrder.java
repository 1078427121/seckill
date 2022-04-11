package com.bytedance.summer.entity;

import java.util.Collection;

/**
 * @author pjl
 * @date 2019/8/16 15:17
 * 返回全部订单用
 */
public class AllOrder {
    private Collection<Order> data;

    public void setData(Collection<Order> data) {
        this.data = data;
    }

    public Collection<Order> getData() {
        return data;
    }
}
