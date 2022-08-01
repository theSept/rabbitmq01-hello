package com.xu.rabbitm.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;
import com.xu.rabbitm.utils.SleepUtils;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 14:31
 * Versions: 1.0.0
 * 消费者1
 */
public class Work3 {

    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2等待接收消息处理时间较长");
        // 收到消息回调
        DeliverCallback deliverCallback = (t,message)->{
            SleepUtils.sleep(30);
            System.out.println("接收到消息："+new String(message.getBody()));
            // 手动应答
            // 参数1：消息的标记 tag
            // 参数2：是否批量应答未应答的消息
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        // 声明取消消息回调
        CancelCallback cancelCallback = a ->{
            System.out.println(a+"消费者取消消费接口回调逻辑");
        };
        // 轮询：0  ； 不公平分发：1 ； 预取值： > 1

        // 设置不公平分发
        // int prefetchCount = 1;
        // 预取值
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        // 接受消息，手动应答
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);

    }
}
