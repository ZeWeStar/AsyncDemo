package com.zewe.asyncboot.entity.trade;

import com.alibaba.fastjson.JSONObject;
import com.zewe.asyncboot.entity.TaskBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 11:33
 */
public class TradeTaskBean extends TaskBean {
    @Override
    public void action() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"code\":0,\"message\":\"交易成功\",\"responseTime\":\""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
