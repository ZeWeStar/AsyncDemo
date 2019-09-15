package com.zewe.asyncboot.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 16:47
 */
public class CommonFactory {
    private static final String propertyPath = "factory.properties";
    public static Factory createFactory(String json)
    {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String commandId=jsonObject.getString("commandId");
        Factory factory=null;
        InputStream in = CommonFactory.class.getClassLoader().getResourceAsStream(propertyPath);
        Properties prop = new Properties();
        try
        {
            prop.load(in);
            String factoryname= prop.getProperty(commandId).trim();
            Class clz=Class.forName(factoryname);
            factory= (Factory) clz.newInstance();
            return factory;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return factory;
    }


}
