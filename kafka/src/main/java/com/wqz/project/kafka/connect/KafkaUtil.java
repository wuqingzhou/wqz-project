package com.wqz.project.kafka.connect;

import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.security.JaasUtils;
import scala.collection.JavaConversions;

import java.util.*;

public class KafkaUtil {
    public static void main(String[] args) {
        String zookeeperStr = "192.168.238.128:2181";
        String topicName = "mytopic1";
        ZkUtils zkUtils = ZkUtils.apply(zookeeperStr, 30000, 30000, JaasUtils.isZkSecurityEnabled());
//        AdminUtils.deleteTopic(zkUtils, topicName);
        List<String> topicList = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        System.out.println(topicList.size());
        for (String name : topicList) {
            System.out.println(name);
        }
    }

    /**
     * 通过zookeeperUrl获取topic列表。
     * zookeeperUrl示例：192.168.238.128:2181
     */
    public static List<String> getTopicList(String zookeeperUrl) {
        List<String> list = new ArrayList<>();
        ZkUtils zkUtils = ZkUtils.apply(zookeeperUrl, 30000, 30000, JaasUtils.isZkSecurityEnabled());
        List<String> topicList = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        for (String name : topicList) {
            list.add(name);
        }
        return list;
    }

    /**
     * 创建topic
     * zookeeperUrl示例：192.168.238.128:2181
     * topicName: topic名
     * partitions： 分区数
     * replicationFactor： 副本数
     */
    public static void createTopic(String zookeeperUrl, String topicName, int partitions, int replicationFactor) {
        ZkUtils zkUtils = ZkUtils.apply(zookeeperUrl, 30000, 30000, JaasUtils.isZkSecurityEnabled());
        AdminUtils.createTopic(zkUtils, topicName, partitions, replicationFactor, new Properties());
    }

    /**
     * 删除topic
     * zookeeperUrl示例：192.168.238.128:2181
     * topicName: topic名
     */
    public static void delTopic(String zookeeperUrl, String topicName) {
        ZkUtils zkUtils = ZkUtils.apply(zookeeperUrl, 30000, 30000, JaasUtils.isZkSecurityEnabled());
        AdminUtils.deleteTopic(zkUtils, topicName);
    }

    /**
     * 通过生产者往topic中写入数据
     *
     * @param bootstrapServers 示例：192.168.238.128:9092
     * @param topicName
     * @param msg
     */
    public static void producerData(String bootstrapServers, String topicName, String msg) {
        String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
        String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
        Properties properties = new Properties();

        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", keySerializer);
        properties.put("value.serializer", valueSerializer);

        Producer<String, String> producer = null;
        try {
            producer = new KafkaProducer<String, String>(properties);
            for (int i = 0; i < 1; i++) {
                producer.send(new ProducerRecord<String, String>(topicName, msg));
                System.out.println("消息发送成功。");
            }
        } catch (Exception e) {
            System.out.println("消息发送失败。");
        } finally {
            producer.close();
        }
    }

    /**
     * 消费topic数据
     *
     * @param bootstrapServers
     * @param topicName
     */
    public static List<String> consumerData(String bootstrapServers, String topicName) {
        List<String> resultList = new ArrayList<>();
        String groupId = UUID.randomUUID().toString();
        String keyDeserialize = "org.apache.kafka.common.serialization.StringDeserializer";
        String valueDdeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        Properties properties = new Properties();

        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", keyDeserialize);
        properties.put("value.deserializer", valueDdeserializer);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topicName));
        long curTime = new Date().getTime();
        while (new Date().getTime() - curTime > 1000) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                resultList.add(record.value());
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }
        return resultList;
    }

}
