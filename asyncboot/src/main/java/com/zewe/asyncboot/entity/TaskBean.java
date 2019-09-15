package com.zewe.asyncboot.entity;

import javax.servlet.AsyncContext;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 16:54
 */
public abstract class TaskBean {

    private String commandId;
    /*
    command 类型
     */
    protected String commandType;

    protected String requestTime;
    /**
     * 上下文对象
     */
    protected AsyncContext context;


    public String getRequestTime() {
        return requestTime;
    }

    public AsyncContext getContext() {
        return context;
    }


    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setContext(AsyncContext context) {
        this.context = context;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public abstract void action();

    public abstract String toJson();
}
