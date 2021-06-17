package com.project.mq.producer;

import com.project.mq.config.FlDefaultMQSample;
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

        DefaultMQProducer producer = FlDefaultMQSample.defaultMQProducer();
        String[] tags = FlDefaultMQSample.TAGS_D;
        try {
            try {

                for(int i=0; i<20; i++) {
                    int orderId = i % 10;
                    Message msg = new Message(FlDefaultMQSample.PRODUCER_TOPIC, tags[i % tags.length], "ORDER_KEY_" + i,
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

                    //SendResult的处理
                    //1、记录SendResult日志
                    //2、当前工作模式时SYNC_MASTER,则如果收到SEND_OK,可以认为(99.99%)消息成功存储到broker了
                    //   如果是其他状态，则应该手动重新发送(为了保证顺序，后续的消息发送暂停)，直到当前消息发送成功或者实在是发送不成功则跳过当前消息继续后续的消息

                    //如果发送遇到异常,应该手动重新发送(为了保证顺序，后续的消息发送暂停)，直到当前消息发送成功或者实在是发送不成功则跳过当前消息继续后续的消息，
                    // "发送端不回滚",考虑两种情况1:消息真的发送失败；2:消息发送假失败
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
