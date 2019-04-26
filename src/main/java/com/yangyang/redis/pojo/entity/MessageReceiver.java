package com.yangyang.redis.pojo.entity;

import org.springframework.stereotype.Component;

/**
 * @Author: guozhiyang_vendor
 * @Date: 2019/3/5 16:22
 * @Version 1.0
 */
@Component
public class MessageReceiver {
    /**
     * 接收消息的方法
     */
    public void receiveMessage(String message) {
        //这里是收到通道的消息之后执行的方法
        System.out.println("跑起来了"+message);
    }
}
