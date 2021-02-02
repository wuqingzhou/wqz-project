package com.wqz.project.druid.controller;

import com.wqz.project.kafka.connect.KafkaUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kafka")
public class TestController {

    @GetMapping("/getTopicList")
    @ApiOperation(value = "获取topic列表", notes = "示例参数：192.168.238.128:2181")
    public List<String> getTopicList(@RequestParam String zookeeperStr) {
        return KafkaUtil.getTopicList(zookeeperStr);
    }

    @PostMapping("/createTopic")
    @ApiOperation(value = "创建topic")
    public String createTopic(@RequestParam String zookeeperUrl,
                              @RequestParam String topicName,
                              @RequestParam int partitions,
                              @RequestParam int replicationFactor) {
        KafkaUtil.createTopic(zookeeperUrl, topicName, partitions, replicationFactor);
        return "创建成功";
    }

    @DeleteMapping("/delTopic")
    @ApiOperation(value = "删除topic")
    public String createTopic(@RequestParam String zookeeperUrl, @RequestParam String topicName) {
        KafkaUtil.delTopic(zookeeperUrl, topicName);
        return "删除成功";
    }

    @PostMapping("/producerData")
    @ApiOperation(value = "往topic中写入数据")
    public String producerData(@RequestParam String zookeeperUrl,
                               @RequestParam String topicName,
                               @RequestParam String msg) {
        KafkaUtil.producerData(zookeeperUrl, topicName, msg);
        return "写入成功";
    }

    @GetMapping("/consumerData")
    @ApiOperation(value = "读取topic数据")
    public List<String> consumerData(@RequestParam String zookeeperUrl,
                                     @RequestParam String topicName) {
        return KafkaUtil.consumerData(zookeeperUrl, topicName);
    }
}
