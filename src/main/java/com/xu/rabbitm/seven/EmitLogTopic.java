package com.xu.rabbitm.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * User: 彼暗
 * Date: 2022-07-29
 * Time: 8:00
 * Versions: 1.0.0
 */
public class EmitLogTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /*
            Q1-->绑定的是
                中间带 orange 带 3 个单词的字符串(*.orange.*)
            Q2-->绑定的是
                最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
                第一个单词是 lazy 的多个单词(lazy.#)

        */
        Map<String, String> map = new HashMap<>();
        map.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        map.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        map.put("quick.orange.fox","被队列 Q1 接收到");
        map.put("lazy.brown.fox","被队列 Q2 接收到");
        map.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        map.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        map.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        map.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");



        Set<Map.Entry<String, String>> entries = map.entrySet();

        for (Map.Entry<String, String> entry : entries) {
            // 发送信息
            channel.basicPublish(EXCHANGE_NAME, entry.getKey(), null, entry.getValue().getBytes());
            System.out.println("发送消息："+entry.getValue());
        }

    }
}
