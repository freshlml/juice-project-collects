package com.project.normal.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;

import java.util.Arrays;

public class Lettuces {

    public static void main(String argv[]) throws Exception {
        client_conn_async();
    }

    public static void client_conn() {
        RedisClient redisClient = RedisClient.create("redis://47.98.221.91:6379");
        StatefulRedisConnection<String, String> statefulRedisConnection = redisClient.connect();
        RedisCommands<String, String> redisCommands = statefulRedisConnection.sync();
        System.out.println(redisCommands.get("key2"));

        statefulRedisConnection.close();
        redisClient.shutdown();
    }

    public static void client_conn_async() throws Exception {
        RedisClient redisClient = RedisClient.create("redis://47.98.221.91:6379");
        RedisAsyncCommands<String, String> commands = redisClient.connect().async();
        RedisFuture<String> future = commands.get("key2");

        while(!future.isDone()) {}

        System.out.println(future.get());

    }

    public static void client_conn_cluster() {
        RedisURI node1 = RedisURI.create("47.98.221.91", 6379);
        RedisURI node2 = RedisURI.create("47.98.221.91", 6380);
        RedisURI node3 = RedisURI.create("47.98.221.91", 6381);

        RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2, node3));


    }
}
