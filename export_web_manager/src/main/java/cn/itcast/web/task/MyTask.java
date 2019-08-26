package cn.itcast.web.task;


import java.util.Date;

/**
 * 定时任务的执行
 */
public class MyTask {

    /**
     * 任务方法
     */

    public void execute(){
        // 业务处理：例如，发送邮件
        // 观察是否每5秒执行一次
        System.out.println("当前时间：" + new Date().toLocaleString());
    }
}
