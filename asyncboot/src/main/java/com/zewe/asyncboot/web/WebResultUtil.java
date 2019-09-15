package com.zewe.asyncboot.web;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 16:07
 */
public class WebResultUtil {

    public static WebResult success(Object data){
        return new WebResult(0,"success",data);
    }

    public static WebResult error(Integer code,String message){
        return new WebResult(code,message);
    }

}
