package com.project.mq.producer;

import com.project.mq.config.FlDefaultMQSample;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.io.UnsupportedEncodingException;

public class TransactionalMessagingProducer {

    /**
     * two-phase commit to ensure eventual consistency(最终一致性)
     * Transactional message ensures that the execution of local transaction and the sending of message can be performed atomically
     */
    /**
     *tips:
     * not support schedule and batch message
     *
     * in order to avoid a single message being checked too many times and lead to half queue(半提交队列) message accumulation,
     * we limited the number of checks for a single message to 15 times by default, but user can change this limit by
     * change the "transactionCheckMax" parameter in the configuration of the broker.
     * if one message has been checked over "transactionCheckMax" times, broker will discard(丢弃) this message and print an error log at the same time by default
     * user can change this behavior by override the "AbstractTransactionCheckListener" class.
     *
     * a transactional message will be checked after a certain period of time that determined by parameter "transactionTimeout" in the configuration of broker
     * and user can change this 时间间隔 by set user property "CHECK_IMMUNITY_TIME_IN_SECONDS" when sending transactional message
     * this parameter takes precedence over(优先于) the "transactionTimeout" parameter
     *
     * a transactional message maybe checked more than once
     *
     * a transactional message maybe consumed more than once，RockerMQ中无论普通消息，事务性消息，都有可能出现相同消息重复消费的情况，则消息消费时幂等性保证就很重要了
     *
     * 消息消费发生异常时，通常采用重试机制
     */
    /**
     *status:
     *  TransactionStatus.CommitTransaction, it means allow consumers to consume this message
     *  TransactionStatus.RollbackTransaction, it means message will be deleted and not allowed to consume
     *  TransactionStatus.Unknown, it means broker is need to check back to determine the status
     */


    public static void main(String argv[]) {
        TransactionMQProducer producer = FlDefaultMQSample.defaultTxMQProducer();
        String[] tags = FlDefaultMQSample.TAGS_TX;

        try {
            for(int i=0; i<9; i++) {
                try {

                    Integer msgId = i;
                    Message msg = new Message(FlDefaultMQSample.PRODUCER_TX_TOPIC, tags[i % tags.length], "TX_KEY_" + i,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //sync, executeLocalTransaction在当前线程中执行
                    TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, msgId);
                    System.out.printf("[%s], %s%n", Thread.currentThread().getName(), sendResult);
                    //事务性消息的sendResult不用处理
                    Thread.sleep(10);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                for (int i = 0; i < 1000; i++) {//生产者线程必须存活，在check时
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            producer.shutdown();
        }

    }



}
