package com.xu.rabbitm.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 彼暗
 * Date: 2022-07-29
 * Time: 9:21
 * Versions: 1.0.0
 * 死信队列
 * 消费者2
 */
public class Consumer02 {
    // 死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();


        System.out.println("等待接受死信队列信息.......");
        DeliverCallback deliverCallback = (c, s) -> {
            System.out.println("成功接受：" + new String(s.getBody()));
        };

        // 消费者取消回调
        CancelCallback cancelCallback = c -> {

        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);

    }
}
