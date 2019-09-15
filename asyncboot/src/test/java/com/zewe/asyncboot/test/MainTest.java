package com.zewe.asyncboot.test;

/**
 * @Author: ZeWe
 * @Date: 2019/9/15 11:55
 */
public class MainTest {
    public static void main(String[] args) {
        int [] cnt = Test.getCnt();
        for (int c: cnt) {
            System.out.printf(c+", ");
        }
    }


}
