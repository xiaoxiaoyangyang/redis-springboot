//package com.yangyang.redis.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @Author: guozhiyang_vendor
// * @Date: 2019/3/5 16:25
// * @Version 1.0
// */
//
////定时器
//@EnableScheduling
//@Component
//public class TestSenderController {
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    //向redis消息队列index通道发布消息
//    @Scheduled(fixedRate = 3000)
//    public void sendMessage(){
//        stringRedisTemplate.convertAndSend("chat",String.valueOf(Math.random()));
//    }
//
//}
