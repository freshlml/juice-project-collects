package com.juice.mq.consumer;

import com.juice.mq.config.FlDefaultMQSample;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderedPushConsumer {

    public static void main(String argv[]) {

        DefaultMQPushConsumer consumer = FlDefaultMQSample.mqPushConsumer2();

        //Orderly监听器，每次MessageQueue只会有一个线程消费(锁)
        consumer.registerMessageListener(new MessageListenerOrderly() {
            AtomicLong consumeTimes = new AtomicLong(0);
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(false);
                long value = this.consumeTimes.incrementAndGet();
                ConsumeOrderlyStatus returnType = ConsumeOrderlyStatus.SUCCESS;

                if ((value % 2) == 0) {
                    returnType = ConsumeOrderlyStatus.SUCCESS;  //消费成功
                } else if ((value % 3) == 0) {
                    returnType = ConsumeOrderlyStatus.ROLLBACK;  //回滚到队列头重新消费（autoCommit=false时）
                } else if ((value % 4) == 0) {
                    returnType = ConsumeOrderlyStatus.COMMIT;    //commit消费进度(属于成功消费)
                } else if ((value % 5) == 0) {
                    context.setSuspendCurrentQueueTimeMillis(3000); //设置当前队列暂停的时间
                    returnType = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT; //暂停当前队列一段时间，暂停时间内，队列其余的消息暂停消费，不断重试原消息的消费(无最大重复次数限制)
                }

                try {
                    System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + ",size=" + msgs.size() + ",msg1=" + new String(msgs.get(0).getBody(), RemotingHelper.DEFAULT_CHARSET) + ",value: " + value + ",returnType: " + returnType + "%n");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return returnType;
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
