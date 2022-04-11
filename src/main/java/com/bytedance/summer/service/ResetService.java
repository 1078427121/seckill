package com.bytedance.summer.service;

import com.bytedance.summer.entity.ResetResult;

public interface ResetService {
    ResetResult reset(String token);
}
