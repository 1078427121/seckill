package com.bytedance.summer.service;

import com.bytedance.summer.entity.PayInput;

/**
 * @author pjl
 * @date 2019/8/16 17:10
 */
public interface TokenGetter {
    String getToken(PayInput payInput);
}
