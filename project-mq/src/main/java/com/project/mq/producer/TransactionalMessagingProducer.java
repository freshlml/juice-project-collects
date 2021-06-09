package com.project.mq.producer;

import com.project.mq.constants.MQConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

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
     * user change this behavior by override the "AbstractTransactionCheckListener" class.
     *
     * a transactional message will be checked after a certain period of time that determined by parameter "transactionTimeout" in the configuration of broker
     * and user can change this 时间间隔 by set user property "CHECK_IMMUNITY_TIME_IN_SECONDS" when sending transactional message
     * this parameter takes precedence over(优先于) the "transactionTimeout" parameter
     *
     * a transactional message maybe checked more than once
     *
     * a transactional message maybe consumed more than once，RockerMQ中无论普通消息，事务性消息，都有可能出现相同消息重复消费的情况，则消息消费时幂等性保证就很重要了
     *
     */
    /**
     *status:
     *  TransactionStatus.CommitTransaction, it means allow consumers to consume this message
     *  TransactionStatus.RollbackTransaction, it means message will be deleted and not allowed to consume
     *  TransactionStatus.Unknown, it means broker is need to check back to determine the status
     */


    public static void main(String argv[]) {
        //创建Transaction producer, with producer group，broker可以联系同一生产组中的不同生产者实例来check
        TransactionMQProducer producer = new TransactionMQProducer(MQConstants.DEFAULT_TRANSACTION_PRODUCER_GROUP);

        //设置nameserver
        producer.setNamesrvAddr(MQConstants.LOCAL_SINGLE_NAMESERVER);

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);

        //设置TransactionListener
        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return ;
        }

        try {
            String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
            for(int i=0; i<9; i++) {
                try {

                    Integer msgId = i;
                    Message msg = new Message(MQConstants.DEFAULT_TRANSACTION_TOPIC, tags[i % tags.length], "KEY" + i,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //sync, executeLocalTransaction在当前线程中执行
                    TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, msgId);
                    System.out.printf("[%s], %s%n", Thread.currentThread().getName(), sendResult);
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

    private static class TransactionListenerImpl implements TransactionListener {

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            Integer msgId = (Integer) arg;
            System.out.printf("[%s], %d%n", Thread.currentThread().getName(), msgId);
            localTrans.put(msg.getTransactionId(), msgId % 3);
            return LocalTransactionState.UNKNOW;
        }
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            System.out.printf("[%s], [%s,%s,%d]%n", Thread.currentThread().getName(), msg.getTransactionId(), new String(msg.getBody(), Charset.forName(RemotingHelper.DEFAULT_CHARSET)), status);
            if(status != null) {
                switch (status) {
                    case 0:
                        return LocalTransactionState.UNKNOW;
                    case 1:
                        return LocalTransactionState.COMMIT_MESSAGE;
                    case 2:
                        return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }

}
