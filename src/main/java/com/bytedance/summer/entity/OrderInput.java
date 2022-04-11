package com.bytedance.summer.entity;

/**
 * @author pjl
 * @date 2019/8/16 19:01
 */
public class OrderInput {
    private Integer uid;
    private Long  pid;

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getUid() {
        return uid;
    }

    public Long getPid() {
        return pid;
    }
}
