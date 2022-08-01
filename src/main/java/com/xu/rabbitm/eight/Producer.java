package com.xu.rabbitm.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xu.rabbitm.utils.RabbitMqUtils;

/**
 * User: 彼暗
 * Date: 2022-07-29
 * Time: 9:45
 * Versions: 1.0.0
 * 死信队列，生产者
 */
public class Producer {
    // 普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 死信消息，设置 TTL 时间
//        AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                .builder()
//                .expiration("10000") // 十秒过期
//                .build();
        for (int i = 0; i < 10; i++) {
            String msg = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,msg.getBytes());
        }
    }
}
