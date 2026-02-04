package com.juice.mq.producer;

import com.juice.mq.config.FlDefaultMQSample;
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

        DefaultMQProducer producer = FlDefaultMQSample.defaultMQProducer();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        String[] tags = FlDefaultMQSample.TAGS_B;
        int messageCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        try {
            for(int i=0; i<messageCount; i++) {
                try {
                    final int index = i;
                    Message msg = new Message(FlDefaultMQSample.PRODUCER_TOPIC,
                                         tags[i % tags.length],
                                    "ASYNC_KEY_" + i,
                                        "Hello 中".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //send async,发送任务提交到线程池中执行
                    producer.send(msg, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            countDownLatch.countDown();
                            System.out.printf("[%s] %-10d OK %s %n", Thread.currentThread().getName(), index, sendResult.getMsgId());
                            //SendResult的处理: 见SyncProducer
                        }
                        @Override
                        public void onException(Throwable e) {
                            //如果发生异常: 见SyncProducer
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
