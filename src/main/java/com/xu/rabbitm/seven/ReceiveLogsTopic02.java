package com.xu.rabbitm.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

/**
 * User: 彼暗
 * Date: 2022-07-29
 * Time: 7:45
 * Versions: 1.0.0
 * 消费者01 主题模式
 */
public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 创建队列
        channel.queueDeclare("Q2", false, false, true, null);
        // 队列绑定交换机
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");

        System.out.println("Q2队列准备推送消息.....");

        // 成功收到消息回调
        DeliverCallback deliverCallback = (c, s) -> {
            System.out.println("收到消息：" + new String(s.getBody()));
//            System.out.println("接收队列Q2："+",绑定键："+s.getEnvelope().getRoutingKey());
        };

        // 消息者被取消时回调
        CancelCallback cancelCallback = c -> {
            System.out.println("消费消息终端");

        };

        // 接受信息
        channel.basicConsume("Q2", true, deliverCallback, cancelCallback);


    }

}
