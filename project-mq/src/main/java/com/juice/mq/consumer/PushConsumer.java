package com.juice.mq.consumer;

import com.juice.mq.config.FlDefaultMQSample;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import java.util.List;

public class PushConsumer {

    public static void main(String argv[]) {

        DefaultMQPushConsumer consumer = FlDefaultMQSample.mqPushConsumer1();

        //注册消费监听器,一个Consumer，内部使用线程池，对所有messageQueue上的消息多线程消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("[%s] Receive New Messages: %s , %d %n", Thread.currentThread().getName(), msgs, msgs.size());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            }
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return ;
        }

        System.out.printf("Consumer Started.%n");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.printf("Consumer Shutdown.%n");
                consumer.shutdown();
            }
        }));
    }


}
