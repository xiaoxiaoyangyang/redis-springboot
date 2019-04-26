package com.yangyang.redis.config;

import com.yangyang.redis.pojo.entity.MessageReceiver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @Author: guozhiyang_vendor
 * @Date: 2019/2/20 13:42
 * @Version 1.0
 */
@Configuration
@AutoConfigureAfter(RedisConfiguration.class)
public class RedisConfiguration {
    @Bean(name = "re")
    public RedisTemplate<String,Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        //键序列化
        template.setKeySerializer(new StringRedisSerializer());
        //值序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

//    @Bean
//    RedisMessageListenerContainer container(LettuceConnectionFactory connectionFactory,
//                                            MessageListenerAdapter listenerAdapter) {
//
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        //订阅了一个叫chat 的通道
//        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
//        //这个container 可以添加多个 messageListener
////        container.addMessageListener(listenerAdapter, new PatternTopic("这里是监听的通道的名字"));
//        return container;
//    }
//
//
//    //利用反射来创建监听到消息之后的执行方法
//    @Bean
//    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
//        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
//        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
//        return new MessageListenerAdapter(receiver, "receiveMessage");//这里2个参数和四一致
//    }


}
