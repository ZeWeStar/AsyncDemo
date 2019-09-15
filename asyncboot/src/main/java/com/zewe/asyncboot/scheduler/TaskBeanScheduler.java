package com.zewe.asyncboot.scheduler;

import com.zewe.asyncboot.entity.TaskBean;
import com.zewe.asyncboot.utils.PropertyUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import java.util.LinkedList;

/**
 * <p>任务调度器<p/>
 * @Author: ZeWe
 * @Date: 2019/9/15 11:41
 */
@Slf4j
public class TaskBeanScheduler {

    /**
     * 工作线程数
     */
    private static final int WORK_THREAD_COUNT = 8;

    /**
     * 优先队列数目，即优先级数目
     */
    private static final int BUSINESS_LEVEL_COUNT = 3;

    /**
     * 优先队级权重
     */
    private static final int[] BUSINESS_LEVEL_WEIGHT = {8, 4, 2};

    /**
     * 优先级最大任务数
     */
    private static final int[] BUSINESS_TASK_MAX_COUNT = {400,600,1000};

    /**
     * 任务队列
     */
    private static BusinessTask<LinkedList<TaskBean>>  BUSINESS_TASKS = new BusinessTask<LinkedList<TaskBean>>(BUSINESS_LEVEL_COUNT);
    /**
     * 优先级权重计数器
     */
    private static int[] BUSINESS_LEVEL_TIMES = new int[BUSINESS_LEVEL_COUNT];

    /**
     * 静态次初始化
     */
    static{
        for(int i=0; i<BUSINESS_LEVEL_COUNT; i++){
            BUSINESS_LEVEL_TIMES[i] = BUSINESS_LEVEL_WEIGHT[i]; // 初始化权重计数器
            BUSINESS_TASKS.setElement(i,new LinkedList<TaskBean>()); // 初始化任务队列
        }
        // 初始化工作线程
        for(int i=0; i<WORK_THREAD_COUNT; i++){
            new WorkThread().start();
        }
    }

    /**
     * 优先级队列插入一个队列
     * @param level 优先级数
     * @param taskBean
     * @return
     */
    public static boolean addTask(int level, TaskBean taskBean){
        if(level < 0 || level >= BUSINESS_LEVEL_COUNT){
            return false;
        }
        int totalCount = 0;
        synchronized (BUSINESS_TASKS){
            totalCount = getTotalTaskSizeNoLock();
            LinkedList<TaskBean> list = BUSINESS_TASKS.getElement(level);
            int currentCnt = list.size();
            int oneTaskMaxCount = BUSINESS_TASK_MAX_COUNT[level];
            if(currentCnt < oneTaskMaxCount){
                list.add(taskBean);
                log.info("优先级对列成功加入一个任务；level:{}, count:{}",level,(currentCnt+1));
            }else{
               log.warn("优先级队列已到达最大任务数；level:{}, max:{} ",level,oneTaskMaxCount);
               return false;
            }

            //若优先级队列原数为0，现在加一个TaskBean, 唤醒工作线程 (!=0 工作线程不wait)
            if(totalCount == 0){
               BUSINESS_TASKS.notifyAll();
            }
        }

        return true;
    }

    /**
     * 取优先级队列中任务，外部需lock
     * @param level 优先级数
     * @return
     */
    public static TaskBean removeFirstTaskNoLock(int level){
        if(level < 0 || level >= BUSINESS_LEVEL_COUNT){
            return null;
        }
        TaskBean taskBean = null;
        LinkedList<TaskBean> list = BUSINESS_TASKS.getElement(level);
        int currentCnt = list.size();
        if(currentCnt > 0){
            taskBean = list.removeFirst();
            log.info("优先级队列成功取出一个任务；level:{}, count:{} ",level,(currentCnt-1));
        }
        return taskBean;
    }

    /**
     * 取优先级队列中任务
     * @param level
     * @return
     */
    public static TaskBean removeFirstTask(int level){
        if(level < 0 || level >= BUSINESS_LEVEL_COUNT){
            return null;
        }
        TaskBean taskBean = null;
        synchronized (BUSINESS_TASKS){
            LinkedList<TaskBean> list = BUSINESS_TASKS.getElement(level);
            int currentCnt = list.size();
            if(currentCnt > 0){
                taskBean = list.removeFirst();
                log.info("优先级队列成功取出一个任务；level:{}, count:{} ",level,(currentCnt-1));
            }
        }
        return taskBean;
    }

    /**
     * 优先级队列总长度，外部需lock
     * @return
     */
    public static int getTotalTaskSizeNoLock() {
        int count = 0;
        for (int i = 0; i < BUSINESS_LEVEL_COUNT; i++) {
            count += BUSINESS_TASKS.getElement(i).size();
        }
        return count;
    }

    /**
     * 单个优先级长度 外部需Lock
     * @param level
     * @return
     */
    public static int getTaskSizeNoLock(int level){
        return BUSINESS_TASKS.getElement(level).size();
    }


    /**
     * 工作线程运行标志
     */
    private static boolean isRunning = true;

    public static void stopWorkThrea(){
        isRunning = false;
    }

    /**
     * 工作线程
     */
    static class WorkThread extends Thread{
        @Override
        public void run() {
            while(isRunning){
                boolean isTaskExist = false;
                TaskBean taskBean = null;
                synchronized (BUSINESS_TASKS){
                    int taskCount = 0;
                    int totalTaskCount = 0;
                    for (int i=0; i<BUSINESS_LEVEL_COUNT; i++){
                        taskCount = getTaskSizeNoLock(i);
                        totalTaskCount+=taskCount;
                        if(BUSINESS_LEVEL_TIMES[i] > 0 && taskCount > 0){
                            isTaskExist = true;
                            taskBean = removeFirstTaskNoLock(i);
                            BUSINESS_LEVEL_TIMES[i]--; // 权重计数减1
                            break;
                        }
                    }

                    if(!isTaskExist){
                        // 按照权重算法未取到任务 重置权重计数器
                        for (int i=0; i<BUSINESS_LEVEL_COUNT; i++){
                            BUSINESS_LEVEL_TIMES[i] = BUSINESS_LEVEL_WEIGHT[i];
                        }
                        // 没有任务，线程等待
                        if(totalTaskCount == 0){
                            try {
                                BUSINESS_TASKS.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(isTaskExist){
                    work(taskBean);
                }
            }
        }

        /**
         * 处理任务
         * @param taskBean
         */
        public static void work(TaskBean taskBean) {
            taskBean.action();
            AsyncContext context = taskBean.getContext();
            // 处理结果放入 request中
            context.getRequest().setAttribute("resultJson",taskBean.toJson());
            context.complete(); //回调
        }
    }
}
