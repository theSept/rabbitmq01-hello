package com.xu.rabbitm.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 7:55
 * Versions: 1.0.0
 * 消费者
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.2.128");
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();

        // 声明 接受消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 声明取消消息回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };

        /*
            消费者消费消息
            参数1：消费哪个队列
            参数2：消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
            参数3：消费者未成功的回调
            参数4：消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
