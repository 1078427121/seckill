package com.bytedance.summer.entity;

/**
 * @author pjl
 * @date 2019/8/12 15:43
 * 支付信息
 */
public class PayResult {
    private Integer code;
    private String token;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }
}
