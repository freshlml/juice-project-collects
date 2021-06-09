package com.project.mq.producer;

import com.project.mq.constants.MQConstants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class OrderedProducer {

    public static void main(String argv[]) {

        DefaultMQProducer producer = new DefaultMQProducer(MQConstants.DEFAULT_PRODUCER_GROUP);

        producer.setNamesrvAddr(MQConstants.LOCAL_SINGLE_NAMESERVER);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return ;
        }

        try {
            try {
                String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
                for(int i=0; i<20; i++) {
                    int orderId = i % 10;
                    Message msg = new Message(MQConstants.DEFAULT_ORDERED_TOPIC, tags[i % tags.length], "KEY" + i,
                            ("Hello 中 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                            @Override
                            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                                Integer id = (Integer) arg;//the orderId
                                int index = id % mqs.size();
                                return mqs.get(index);
                            }
                    }, orderId);
                    System.out.printf("%s, msg: %s%n", sendResult, new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET));
                }
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            }
        } finally {
            producer.shutdown();
        }

    }

}
