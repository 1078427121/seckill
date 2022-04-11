package com.bytedance.summer.entity;

public class ProductRedis {
    private Long pid;
    private Integer count;

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getPid() {
        return pid;
    }

    public Integer getCount() {
        return count;
    }
}
