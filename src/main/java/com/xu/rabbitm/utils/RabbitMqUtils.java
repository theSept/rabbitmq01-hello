package com.xu.rabbitm.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 8:12
 * Versions: 1.0.0
 * RabbitMq工具类
 */
public class RabbitMqUtils {

    /**
     * 获取RabbitMq 的连接信道 channel
     *
     */
    public static Channel getChannel() throws Exception {
        // 创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.128");
        factory.setUsername("admin");
        factory.setPassword("123");
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        return connection.createChannel();
    }
}
