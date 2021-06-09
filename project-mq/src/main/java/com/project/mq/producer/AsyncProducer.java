package com.project.mq.producer;

import com.project.mq.constants.MQConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

public class AsyncProducer {

    public static void main(String argv[]) {
        //创建Producer，with producer group
        DefaultMQProducer producer = new DefaultMQProducer(MQConstants.DEFAULT_PRODUCER_GROUP);
        //设置nameserver
        producer.setNamesrvAddr(MQConstants.LOCAL_SINGLE_NAMESERVER);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return ;
        }
        producer.setRetryTimesWhenSendAsyncFailed(0);


        int messageCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        try {
            for(int i=0; i<messageCount; i++) {
                try {
                    final int index = i;
                    Message msg = new Message(MQConstants.DEFAULT_TOPIC, MQConstants.ASYNC_TAG,
                            "Hello 中".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //send async,发送任务提交到线程池中执行
                    producer.send(msg, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            countDownLatch.countDown();
                            System.out.printf("[%s] %-10d OK %s %n", Thread.currentThread().getName(), index, sendResult.getMsgId());
                        }
                        @Override
                        public void onException(Throwable e) {
                            countDownLatch.countDown();
                            System.out.printf("[%s] %-10d Exception %s %n", Thread.currentThread().getName(), index, e);
                            e.printStackTrace();
                        }
                    });
                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            producer.shutdown();
        }

    }


}
