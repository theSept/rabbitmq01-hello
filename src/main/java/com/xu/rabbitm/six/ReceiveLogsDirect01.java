package com.xu.rabbitm.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 22:45
 * Versions: 1.0.0
 *
 */
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明队列
        channel.queueDeclare("console",false,false,true,null);
        // 队列绑定交换机
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        // 消息成功回调
        DeliverCallback deliverCallback = (c, s)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的信息："+new String(s.getBody()));
        };

        // 消费者被取消回调
        CancelCallback cancelCallback = (s)->{

        };
        // 消息消费
        channel.basicConsume("console",true,deliverCallback,cancelCallback);


    }
}
