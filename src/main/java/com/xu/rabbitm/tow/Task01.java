package com.xu.rabbitm.tow;

import com.rabbitmq.client.Channel;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 12:00
 * Versions: 1.0.0
 * 生产者
 */
public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 声明一个队列
        // 参数1：队列名称
        // 参数2：队列里面的消息是否持久化（磁盘），默认情况消息是存储在内存中
        // 参数3：该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费
        // 参数4：是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);


        // 发送信息
        // 参数1：发送到哪个交换机
        // 参数2：路由的key值是哪个 本次是队列的名称
        // 参数3：其他参数信息
        // 发送消息的消息体
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String msg = input.next();
            // 参数1：发送到哪个交换机
            // 参数2：路由的key值是哪个 本次是队列的名称
            // 参数3：其他参数信息
            // 参数4：发送消息的消息体
            channel.basicPublish("",QUEUE_NAME,null, msg.getBytes());
            System.out.println("发送成功:"+msg);
        }

    }
}
