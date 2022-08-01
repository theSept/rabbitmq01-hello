package com.xu.rabbitm.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 14:24
 * Versions: 1.0.0
 * 生产者
 *
 *  消息在手动应答是不丢失的，放回队列当中重新消费
 */
public class Task02 {
    // 队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        // 消息持久化
        // 声明队列时设置 持久化 为 true
        // 发送信息属性 props 设置为 MessageProperties.PERSISTENT_TEXT_PLAIN

        // 声明一个队列
        // 参数1：队列名称
        // 参数2：队列里面的消息是否持久化（磁盘），默认情况消息是存储在内存中
        // 参数3：该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费
        // 参数4：是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable,false,false,null);


        // 从控制台中输入值
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String msg = input.next();

            // 发送信息
            // 设置生产者发送消息为持久化消息（要求保存到磁盘上），默认保存在内存中
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN
                    ,msg.getBytes("UTF-8"));
            System.out.println("发送消息："+msg);
        }

    }

}
