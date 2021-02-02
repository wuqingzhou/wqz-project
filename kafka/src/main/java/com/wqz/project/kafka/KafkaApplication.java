package com.wqz.project.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class KafkaApplication {
    public static void main(String[] args) {
        System.out.println("============================================== 开始启动 kafka 项目。");
        SpringApplication.run(KafkaApplication.class, args);
    }
}
