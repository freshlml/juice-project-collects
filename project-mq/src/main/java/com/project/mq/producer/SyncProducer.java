package com.project.mq.producer;

import com.project.mq.constants.MQConstants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.io.UnsupportedEncodingException;

public class SyncProducer {


    public static void main(String argv[]) {
        //创建Producer，with producer group name
        DefaultMQProducer producer = new DefaultMQProducer(MQConstants.DEFAULT_PRODUCER_GROUP);

        //指定nameserver
        producer.setNamesrvAddr(MQConstants.LOCAL_SINGLE_NAMESERVER);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return ;
        }
        //producer.setRetryTimesWhenSendFailed(1);

        try {
            for(int i=0; i<100; i++) {
                try {
                    //创建Message，with topic and message body
                    Message msg = new Message(MQConstants.DEFAULT_TOPIC, MQConstants.SYNC_TAG,
                            "Hello 中".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //sync send message
                    //1、发送之前check最大message size，当大于defaultMQProducer.getMaxMessageSize()时，抛出MQClientException
                    //2、发送之前check topic信息，如果没有此topic，抛出MQClientException("No route info of this topic")
                    //3、计算times(重试次数)： sync模式，1+defaultMQProducer.getRetryTimesWhenSendFailed()，其他：1
                    //4、MessageQueueSelector逻辑,如果messageQueueSelector返回null，抛出MQClientException
                    //5、同步发送，带超时，如果超时，抛出RemotingException，则运行times重试次数，如果发送方法抛出异常，运行times重试次数
                    //   发送返回SEND_OK，返回；如果返回非SEND_OK，defaultMQProducer.retryAnotherBrokerWhenNotStoreOK=true时,运行times重试
                    //SYNC模式下，由于重试机制，可能导致broker中存在重复的消息
                    SendResult result = producer.send(msg);
                    //SendStatus
                    //SEND_OK,成功标记;SYNC_FLUSH下同步刷到磁盘才返回此状态,SYNC_MASTER下同步复制到slave才返回此状态
                    //               ASYNC_FLUSH下写到内存缓冲区即返回,ASYNC_MASTER下写到内存缓冲区即返回
                    //FlushDiskType=SYNC_FLUSH，如果刷盘用时>MessageStoreConfig.syncFlushTimeout，则返回FLUSH_DISK_TIMEOUT
                    //Broker's role=SYNC_MASTER，如果同步复制到slave用时>MessageStoreConfig.syncFlushTimeout，则返回FLUSH_SLAVE_TIMEOUT
                    //Broker's role=SYNC_MASTER，如果没有slave，返回SLAVE_NOT_AVAILABLE
                    System.out.printf("%s%n", result);
                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (MQBrokerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            producer.shutdown();
        }

    }


}
