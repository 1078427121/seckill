package com.bytedance.summer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bytedance.summer.entity.PayInput;
import com.bytedance.summer.service.TokenGetter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author pjl
 * @date 2019/8/16 16:03
 */
@ConfigurationProperties(prefix = "tokenservice")
@Service
public class TokenGetterIMPL implements TokenGetter {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 获取Token
     */
    public String getToken(PayInput payInput) throws NullPointerException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonString = JSONObject.toJSONString(payInput);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);
        RestTemplate client = new RestTemplate();
        ResponseEntity<String> response = client.postForEntity(url,request, String.class);
        Map map = (Map) JSON.parse(response.getBody());
        if (map != null) {
            return map.get("token").toString();
        }
        return "";
    }
}
