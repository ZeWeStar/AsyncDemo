package com.zewe.asyncboot.entity;

import javax.servlet.AsyncContext;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 16:54
 */
public interface Factory {

    TaskBean createTaskBean(String json);

    TaskBean createTaskBean(String json, AsyncContext context);
}
