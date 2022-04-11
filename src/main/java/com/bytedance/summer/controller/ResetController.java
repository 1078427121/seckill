package com.bytedance.summer.controller;

import com.bytedance.summer.debug.CountTime;
import com.bytedance.summer.entity.ResetInput;
import com.bytedance.summer.entity.ResetResult;
import com.bytedance.summer.exception.HttpForbiddenException;
import com.bytedance.summer.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetController{
    @Autowired
    private ResetService resetService;

//    @CountTime
    @PostMapping(value = "/reset")
    public ResetResult reset(@RequestBody ResetInput resetInput){
        try{
            return resetService.reset(resetInput.getToken());
        }catch (Exception e){
            e.printStackTrace();
            throw new HttpForbiddenException("未知错误");
        }
    }
}
