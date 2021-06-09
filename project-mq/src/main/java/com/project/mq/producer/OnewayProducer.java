package com.project.mq.producer;

import com.project.mq.constants.MQConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class OnewayProducer {


    public static void main(String argv[]) throws UnsupportedEncodingException {
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
        try {
            for (int i = 0; i < 100; i++) {
                try {
                    Message msg = new Message(MQConstants.DEFAULT_TOPIC, MQConstants.ONEWAY_TAG,
                            "Hello 中".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //send, async发送，将message发给broker，不会等待broker的响应
                    //1、发送之前check最大message size，当大于defaultMQProducer.getMaxMessageSize()时，抛出MQClientException
                    //2、发送之前check topic信息，如果没有此topic，抛出MQClientException("No route info of this topic")
                    //3、计算times(重试次数)： oneway模式: 1
                    //4、MessageQueueSelector逻辑,如果messageQueueSelector返回null，抛出MQClientException
                    //5、异步发送，带超时，如果超时，抛出RemotingException
                    // 异步发送给broker，不会等待broker的响应，不能保证message被broker正确存储
                    producer.sendOneway(msg);
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
