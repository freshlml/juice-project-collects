package com.fresh.juice.mq.producer;

import com.fresh.juice.mq.config.FlDefaultMQSample;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.io.UnsupportedEncodingException;

public class OnewayProducer {


    public static void main(String argv[]) {
        DefaultMQProducer producer = FlDefaultMQSample.defaultMQProducer();

        String[] tags = FlDefaultMQSample.TAGS_C;
        try {
            for (int i = 0; i < 100; i++) {
                try {
                    Message msg = new Message(FlDefaultMQSample.PRODUCER_TOPIC,
                            tags[i % tags.length],
                            "ONEWAY_KEY_" + i,
                            "Hello 中".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //send, async发送，将message发给broker，不会等待broker的响应
                    //1、发送之前check最大message size，当大于defaultMQProducer.getMaxMessageSize()时，抛出MQClientException
                    //2、发送之前check topic信息，如果没有此topic，抛出MQClientException("No route info of this topic")
                    //3、计算times(重试次数)： oneway模式: 1
                    //4、MessageQueueSelector逻辑,如果messageQueueSelector返回null，抛出MQClientException
                    //5、异步发送，带超时，如果超时，抛出RemotingException
                    // 异步发送给broker，不会等待broker的响应，不能保证message被broker正确存储
                    producer.sendOneway(msg);

                    //没有返回值处理

                    //抛出异常也没关系
                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                Thread.sleep(5000); //wait for send to complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            producer.shutdown();
        }

    }
}
