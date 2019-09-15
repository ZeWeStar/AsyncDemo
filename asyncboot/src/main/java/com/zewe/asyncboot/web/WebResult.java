package com.zewe.asyncboot.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 16:03
 */
@Getter
@Setter
public class WebResult {

    private Integer code;
    private String msg;
    private Object data;

    public WebResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public WebResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebResult() {
    }
}
