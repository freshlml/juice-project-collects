package com.fresh.juice.mq.config;

import com.fresh.juice.mq.constants.MQConstants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * 原则
 *      * 1、一个应用实例化一个Producer类，一个应用部署多个节点，多个节点中的Producer属于同一Producer Group
 *      * 2、一个应用一个topic，用于发送消息(非事务性消息)，不同消息类型一般使用tag标记
 *      * 3、一个应用实例化一个tx Producer类，一个应用部署多个节点，多个节点中的tx Producer属于同一tx Producer Group
 *      * 4、一个应用一个tx topic，用于发送事务性消息，不同类型的事务性消息一般使用tag标记
 *      * 5、发送消息方法调用一般是在业务代码执行最后
 *      * 6、消费模式: 假设现在要消费 topic[tag_a,tag_b]; 如果tag_a,tag_b分开来消费
 *      *  1)、定义一个消费组，里面的消费者有些消费tag_a,有些消费tag_b，这是有问题的
 *      *  2)、定义两个消费组，consume_group_a,consume_group_b
 *      *      consume_group_a中的所有消费者订阅topic, tag_a
 *      *      consume_group_b中的所有消费者订阅topic, tag_b
 *      *  即，一个消费组内的消费组消费行为要一致
 *      * 7、消费注意幂等处理和重试机制
 */
public class FlDefaultMQSample {

    public static String PRODUCER_GROUP = "project-mq-producer-group";
    public static String PRODUCER_TOPIC = "project-mq-topic";

    public static String PRODUCER_TX_GROUP = "project-mq-producer-tx-group";
    public static String PRODUCER_TX_TOPIC = "project-mq-tx-topic";

    public static String[] TAGS_A = new String[]{"A_Tag1", "A_Tag2", "A_Tag3", "A_Tag4", "A_Tag5"};
    public static String[] TAGS_B = new String[]{"B_Tag1", "B_Tag2", "B_Tag3", "B_Tag4", "B_Tag5"};
    public static String[] TAGS_C = new String[]{"C_Tag1", "C_Tag2", "C_Tag3", "C_Tag4", "C_Tag5"};
    public static String[] TAGS_D = new String[]{"D_Tag1", "D_Tag2", "D_Tag3", "D_Tag4", "D_Tag5"};

    public static String[] TAGS_TX = new String[]{"TX_A", "TX_B", "TX_C", "TX_D", "TX_E"};

    public static String CONSUMER_GROUP_A = "consumer_group_a"; //消费组A，该消费组内的所有消费者消费PRODUCER_TOPIC的TAGS_A,TAGS_B,TAGS_C
    public static String CONSUMER_GROUP_B = "consumer_group_b"; //消费组B，该消费组内的所有消费者消费PRODUCER_TOPIC的TAGS_D中的1，3，4

    public static String CONSUMER_GROUP_C = "consumer_group_c"; //消费组C，该消费组内的所有消费者消费PRODUCER_TX_TOPIC的*


    /**
     *1、一个应用实例化一个Producer类，一个应用部署多个节点，多个Producer属于同一Producer Group
     *2、一个应用一个topic，用于发送消息，不同消息类型一般使用tag标记
     * @return
     */
    public static DefaultMQProducer defaultMQProducer() {
        //创建Producer，with producer group
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        //指定nameserver
        producer.setNamesrvAddr(MQConstants.DOCKER_CLUSTER_NAMESERVER);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return null;
        }

        return producer;
    }


    public static DefaultMQPushConsumer mqPushConsumer1() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_A);
        consumer.setNamesrvAddr(MQConstants.DOCKER_CLUSTER_NAMESERVER);
        try {
            //订阅PRODUCER_TOPIC的TAGS_A，TAGS_B，TAGS_C
            consumer.subscribe(PRODUCER_TOPIC, "A_Tag1 || A_Tag2 || A_Tag3 || A_Tag4 || A_Tag5 || B_Tag1 || B_Tag2 || B_Tag3 || B_Tag4 || B_Tag5 || C_Tag1 || C_Tag2 || C_Tag3 || C_Tag4 || C_Tag5");
        } catch (MQClientException e) {
            e.printStackTrace();
            return null;
        }
        return consumer;
    }

    public static DefaultMQPushConsumer mqPushConsumer2() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_B);
        consumer.setNamesrvAddr(MQConstants.DOCKER_CLUSTER_NAMESERVER);
        try {
            //订阅PRODUCER_TOPIC的TAGS_D中的1，3，4
            consumer.subscribe(PRODUCER_TOPIC, "D_Tag1 || D_Tag3 || D_Tag4");
        } catch (MQClientException e) {
            e.printStackTrace();
            return null;
        }
        return consumer;
    }


    public static TransactionMQProducer defaultTxMQProducer() {
        TransactionMQProducer producer = new TransactionMQProducer(PRODUCER_TX_GROUP);
        producer.setNamesrvAddr(MQConstants.DOCKER_CLUSTER_NAMESERVER);

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);

        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return null;
        }

        return producer;
    }

    public static DefaultMQPushConsumer txMqPushConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_C);
        consumer.setNamesrvAddr(MQConstants.DOCKER_CLUSTER_NAMESERVER);
        try {
            //订阅PRODUCER_TX_TOPIC的*
            consumer.subscribe(PRODUCER_TX_TOPIC, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
            return null;
        }
        return consumer;
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
