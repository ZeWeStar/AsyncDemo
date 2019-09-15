package com.zewe.asyncboot.scheduler;

/**
 * 队列外部处理类
 * @Author: ZeWe
 * @Date: 2019/9/15 15:10
 */
public class BusinessTask<E> {
    private E[] elements = null;;
    private int count = 0;

    public BusinessTask(int size){
        elements = (E[])new Object[size];
        count = size;
    }
    public void setElement(int index, E e) {
        if (index >= 0 && index < count) {
            elements[index] = e;
        }
    }

    public E getElement(int index) {
        if (index >= 0 && index < count) {
            return elements[index];
        }

        return null;
    }

}
