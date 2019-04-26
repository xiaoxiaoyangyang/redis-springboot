package com.yangyang.redis.controller;

import com.yangyang.redis.utils.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: guozhiyang_vendor
 * @Date: 2019/4/19 13:21
 * @Version 1.0
 */
@RestController
public class TestController {

    @Autowired
    private RedisLock redisLock;
    static int i=50;
    @GetMapping("bb")
    public ResponseEntity<String> iil(){

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        testguo();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }
    public void testguo() throws InterruptedException {
        long l = System.currentTimeMillis() + 1000;
        boolean xiaohua = redisLock.lock("lili", String.valueOf(l));
        if(xiaohua){
            System.out.println(Thread.currentThread().getName()+"获得了锁");
            System.out.println(--i);
            redisLock.unlock("lili",String.valueOf(l));
            System.out.println(Thread.currentThread().getName()+"释放了锁");
        }
    }
}
