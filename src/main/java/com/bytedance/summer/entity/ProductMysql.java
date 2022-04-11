package com.bytedance.summer.entity;

public class ProductMysql {
    private Long pid;
    private Integer price;
    private String detail;

    public void setPid(Long pid) {
        this.pid = pid;
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

    public Integer getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }
}
