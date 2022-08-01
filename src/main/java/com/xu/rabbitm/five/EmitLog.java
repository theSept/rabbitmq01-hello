package com.xu.rabbitm.five;

import com.rabbitmq.client.AMQP;
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
public class EmitLog {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        // 参数1：exchange 的名称
        // 参数2：exchange 的类型
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String msg = input.next();
            // 发送消息,指定路由key为空字符
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
            System.out.println("生产者成功发送："+msg);
        }

    }
}
