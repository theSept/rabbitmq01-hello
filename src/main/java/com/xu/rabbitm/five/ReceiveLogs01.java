package com.xu.rabbitm.five;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 22:14
 * Versions: 1.0.0
 * 消息接受 1
 */
public class ReceiveLogs01 {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明交换机
         channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        // 声明一个队列
        // 声明一个临时队列：名称是随机的，当消费者断开与队列的连接的时候，队列就会自动删除
        String queue = channel.queueDeclare().getQueue();
        // 交换机与队列绑定
        // 参数1：队列名称
        // 参数2：交换机名称
        // 参数3：路由key
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("01等待消息接受....");

        // 消息成功回调
        DeliverCallback deliverCallback = (c,s)->{
            System.out.println("消息成功接受："+new String(s.getBody()));
        };

        // 消费者被取消回到
        CancelCallback cancelCallback = (s)->{

        };
        // 接受信息
        channel.basicConsume(queue,true,deliverCallback,cancelCallback);

    }


}
