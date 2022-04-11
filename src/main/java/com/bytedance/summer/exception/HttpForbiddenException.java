package com.bytedance.summer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author pjl
 * @date 2019/8/15 20:45
 * http 403
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN,reason="禁止访问")
public class HttpForbiddenException extends RuntimeException {
    private Integer code =1;

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    private static final long serialVersionUID = 1L;

    public HttpForbiddenException() {
        super();
    }

    public HttpForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpForbiddenException(String message) {
        super(message);
    }

    public HttpForbiddenException(Throwable cause) {
        super(cause);
    }
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
