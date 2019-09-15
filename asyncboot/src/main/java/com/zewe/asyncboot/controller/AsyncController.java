package com.zewe.asyncboot.controller;

import com.zewe.asyncboot.entity.CommonFactory;
import com.zewe.asyncboot.entity.Factory;
import com.zewe.asyncboot.web.WebResult;
import com.zewe.asyncboot.web.WebResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ZeWe
 * @Date: 2019/9/14 16:01
 */
@RestController
@Slf4j
public class AsyncController {

    private static final Long timeout = 60 * 1000L ;

    @PostMapping("/async")
    public void async(HttpServletRequest request, HttpServletResponse response){
        String msg = parseMsgString(request);
        log.info("in AsyncController method async -> msg: {}",msg);
        AsyncContext context = request.startAsync(); //请求异步处理
        context.setTimeout(timeout);
        context.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                AsyncContext context = event.getAsyncContext();
                ServletResponse response = context.getResponse();
                String result = (String) context.getRequest().getAttribute("resultJson");
                if(null == result){
                    result = "\"code\":-1,\"message\":\"任务失败\",\"responseTime\":\""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\"";
                }
                log.info("处理后返回客户端报文：{}",result);
                OutputStream outputStreamut = response.getOutputStream();
                outputStreamut.write(result.getBytes("UTF-8"));
                outputStreamut.close();
                response.getOutputStream().close();
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.error("------------- 任务超时 --------------");
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                log.error("------------- 任务出错 --------------");
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("------------- onStartAsync --------------");
            }
        });
        Factory factory = CommonFactory.createFactory(msg);
        factory.createTaskBean(msg,context);
    }

    /**
     * 解析报文
     * @param request
     * @return
     */
    private String parseMsgString(HttpServletRequest request)
    {
        String message= null;
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = request.getInputStream();
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            message = outStream.toString();
        } catch (Exception e) {
            log.error("读取并写入出错:"+e);
        } finally {
            try {
                inStream.close();
                outStream.close();
            } catch (Exception e) {
                log.error("关闭流出错:"+e);
            }
        }
        return message;
    }

}
