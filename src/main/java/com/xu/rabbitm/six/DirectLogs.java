package com.xu.rabbitm.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 22:27
 * Versions: 1.0.0
 * 生产者
 */
public class DirectLogs {
    // 交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String msg = input.next();
            // 发送消息
            channel.basicPublish(EXCHANGE_NAME,"warning",null,msg.getBytes());
            System.out.println("生产者成功发送："+msg);
        }

    }
}
