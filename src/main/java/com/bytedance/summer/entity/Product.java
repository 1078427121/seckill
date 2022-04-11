package com.bytedance.summer.entity;

/**
 * @author pjl
 * @date 2019/8/12 14:25
 * 商品信息
 */
public class Product {
    private Long pid;
    private Integer count;
    private Integer price;
    private String detail;

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getPid() {
        return pid;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }
}
