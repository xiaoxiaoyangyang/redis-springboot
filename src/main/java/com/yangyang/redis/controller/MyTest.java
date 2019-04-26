package com.yangyang.redis.controller;

import java.util.stream.IntStream;

/**
 * @Author: guozhiyang_vendor
 * @Date: 2019/2/20 20:45
 * @Version 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        int sum = IntStream.range(1, 100).sum();
        System.out.println(sum);

    }
}
