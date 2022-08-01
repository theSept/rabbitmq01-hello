package com.xu.rabbitm.tow;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 8:15
 * Versions: 1.0.0
 * 这是一个工作线程（相当于之前的消费者）
 */
public class Worker01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明接受消息回调
        DeliverCallback deliverCallback = (v, s) -> {
            System.out.println("接受到的消息：" + new String(s.getBody()));
        };

        // 声明取消消息回调
        CancelCallback cancelCallback = v -> {
            System.out.println(v + "消费者取消消息接口的回调逻辑");
        };

        /*
            消费者消费消息
            参数1：消费哪个队列
            参数2：消费成功之后是否要自动应答 true 代表的自动应答 false 代表手动应答
            参数3：消费者未成功的回调
            参数4：消费者取消消费的回调
         */
        System.out.println("c3等待接受......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }
}
