package com.zewe.asyncboot.test;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 11:55
 */
public class Test {
    private static final int[] cnt = {1,2,3};

    static {
        cnt[0] = 4;
        cnt[1] = 5;
        cnt[2] = 6;
    }

    public static int[] getCnt(){
        return cnt;
    }
}
