package com.zewe.asyncboot.entity;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 16:37
 */
public class LevelCnf {

    public static int getLevel(String commondType){
        int level = -1;
        switch (commondType){
            case "LOGON":
                level = 0;
                break;
            case "TRADE":
                level = 1;
                break;
            case "QUERY":
                level = 2;
                break;
        }
        return level;
    }
}
