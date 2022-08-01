package com.xu.rabbitm.fouth;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.xu.rabbitm.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * User: 彼暗
 * Date: 2022-07-28
 * Time: 16:30
 * Versions: 1.0.0
 * 发布确认模式
 * -单个确认
 * -批量确认
 * -异步确认
 * 比较哪种确认方式是最好的
 */
public class ConfirmMessage {

    // 总数
    public static final int msg_count = 1000;

    public static void main(String[] args) throws Exception {
        // 单个确认
        // singleAffirm();  // 发布1000条信息，总用时：588ms

        // 批量确认
        // batchAffirm();  // 发布1000条信息，总用时：76ms

        // 异步确认
        asyncAffirm();  // 发布1000条信息，总用时：44ms
                        // 发布1000条信息，总用时：53
    }

    // 单个确认     发布1000条信息，总用时：588ms
    public static void singleAffirm() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();


        String queueName = UUID.randomUUID().toString();

        // 队列的声明
        channel.queueDeclare(queueName, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        // 开始时间
        long l = System.currentTimeMillis();

        for (int i = 0; i < msg_count; i++) {
            String msg = "单个确认发布测试" + i;
            // 发送信息
            channel.basicPublish("", queueName, null, msg.getBytes());
            // 确认发布
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息发送成功");
            }

        }

        long l1 = System.currentTimeMillis();
        System.out.println("发布" + msg_count + "条信息，总用时：" + (l1 - l));
    }


    // 批量确认     发布1000条信息，总用时：76ms
    public static void batchAffirm() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();

        // 队列的声明
        channel.queueDeclare(queueName, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        // 开始时间
        long l = System.currentTimeMillis();

        // 批量确认消息大小
        int batchSize = 100;

        // 批量发布信息，批量发布确认
        for (int i = 0; i < msg_count; i++) {
            String msg = "单个确认发布测试" + i;
            // 发送信息
            channel.basicPublish("", queueName, null, msg.getBytes());
            if (i % batchSize == 0) {
                if (channel.waitForConfirms()) {
                    System.out.println("消息发送成功");
                }
            }

        }

        long l1 = System.currentTimeMillis();
        System.out.println("发布" + msg_count + "条信息，总用时：" + (l1 - l));
    }

    // 异步确认 发布1000条信息，总用时：44ms
    public static void asyncAffirm() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();

        // 队列的声明
        channel.queueDeclare(queueName, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        // 线程安全有序的一个哈希表，适用于高并发的情况
        // 1.轻松的将序号与消息进行关联
        // 2.轻松批量删除条目，只要给到序号
        // 3.支持高并发（多线程）

        ConcurrentSkipListMap<Long,String> map =
                new ConcurrentSkipListMap<>();

        // 消息成功监听回调
        ConfirmCallback confirmCallback = (deliveryTag,multiple)->{
            // 2.删除已经确认的消息 剩下的都是未确认的
            if(multiple){
                // 批量确认删除
                ConcurrentNavigableMap<Long, String> navigableMap = map.headMap(deliveryTag);
                navigableMap.clear();
            }else {
                // 单个确认删除
                map.remove(deliveryTag);
            }
            System.out.println("确认发送的消息："+deliveryTag);

        };
        // 消息失败监听回调
        ConfirmCallback confirmCallbNack = (deliveryTag,multiple)->{

            System.out.println("未确认的消息："+map.get(deliveryTag)+"未确认的消息tag："+ deliveryTag);
        };


        // 准备消息监听器，监听哪些消息成功，哪些失败
        // 参数1：成功回调参数
        // 参数2：失败回调参数
        channel.addConfirmListener(confirmCallback,confirmCallbNack); // 异步通知

        // 开始时间
        long l = System.currentTimeMillis();


        for (int i = 0; i < msg_count; i++) {
            String msg = "发布消息" + i;
            // 发送信息
            channel.basicPublish("", queueName, null, msg.getBytes());

            // 1.记录所有发送的消息
            map.put(channel.getNextPublishSeqNo(),msg);
        }

        long l1 = System.currentTimeMillis();
        System.out.println("发布" + msg_count + "条信息，总用时：" + (l1 - l));

    }

}
