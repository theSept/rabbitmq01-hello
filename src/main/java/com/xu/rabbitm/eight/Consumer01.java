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
 * 消费者1
 */
public class Consumer01 {
    // 普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明普通交换机 类型为 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信交换机 类型为 direct
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);


        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        Map<String, Object> map = new HashMap<>();
        // 过期时间
        // map.put("x-message-ttl",10000);
        // 正常队列设置为死信队列
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        map.put("x-dead-letter-routing-key", "lisi");
        // 设置正常队列的长度，最大长度
        // map.put("x-max-length",6);

        // 声明普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, map);
        // 绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        // 消费消息回调
        DeliverCallback deliverCallback = (c, s) -> {
            String msg = new String(s.getBody());
            if("info5".equals(msg)){
                System.out.println("此消息被拒绝接受："+msg+"，拒接的是C1");
                // 拒绝消息
                channel.basicReject(s.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("成功接受：" + new String(s.getBody()));
                // 手动应答
                channel.basicAck(s.getEnvelope().getDeliveryTag(),false);

            }
        };

        // 消费者取消回调
        CancelCallback cancelCallback = c -> {

        };
        // 消费消息手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);


    }
}
