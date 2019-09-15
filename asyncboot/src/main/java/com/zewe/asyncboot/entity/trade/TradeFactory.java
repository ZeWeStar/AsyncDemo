package com.zewe.asyncboot.entity.trade;

import com.alibaba.fastjson.JSONObject;
import com.zewe.asyncboot.entity.Factory;
import com.zewe.asyncboot.entity.LevelCnf;
import com.zewe.asyncboot.entity.TaskBean;
import com.zewe.asyncboot.scheduler.TaskBeanScheduler;

import javax.servlet.AsyncContext;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 11:34
 */
public class TradeFactory implements Factory {
    @Override
    public TaskBean createTaskBean(String json) {
        return null;
    }

    @Override
    public TaskBean createTaskBean(String json, AsyncContext context) {
        TradeTaskBean taskBean = JSONObject.parseObject(json,TradeTaskBean.class);
        taskBean.setContext(context);
        // 添加到优先级队列中
        TaskBeanScheduler.addTask(LevelCnf.getLevel("TRADE"),taskBean);
        return taskBean;
    }
}
