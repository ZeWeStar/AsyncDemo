package com.zewe.asyncboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 15:54
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/getDate")
    public String test(){
        return "success : "+ new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss").format(new Date());
    }
}
