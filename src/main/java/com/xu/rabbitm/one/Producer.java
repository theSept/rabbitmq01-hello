package com.xu.rabbitm.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 7:40
 * Versions: 1.0.0
 * 生产者：发消息
 */
public class Producer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 工厂ip,连接 Rabbitmq的队列
        factory.setHost("192.168.2.128");

        factory.setUsername("admin");
        factory.setPassword("123");
        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();


        Map<String, Object> map = new HashMap<>();
        //设置队列的最大优先级 最大可以设置到 255 官网推荐 1-10 如果设置太高比较吃内存和 CPU
        map.put("x-max-priority", 10);
        // 生成一个队列
        // 参数1：队列名称
        // 参数2：队列里面的消息是否持久化（磁盘），默认情况消息是存储在内存中
        // 参数3：该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费
        // 参数4：是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除
        channel.queueDeclare(QUEUE_NAME, true, false, false, map);

        for (int i = 0; i < 10; i++) {
            String msg = "info" + i;
            if (i == 5) {
                // 设置优先级
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                        .builder()
                        .priority(3)    // 设置优先级为3
                        .build();
                channel.basicPublish("", QUEUE_NAME, basicProperties, msg.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            }
        }

        // 发消息
        String message = "hello world ";
        // 参数1：发送到哪个交换机
        // 参数2：路由的key值是哪个 本次是队列的名称
        // 参数3：其他参数信息
        // 发送消息的消息体
        // channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");

    }

}
