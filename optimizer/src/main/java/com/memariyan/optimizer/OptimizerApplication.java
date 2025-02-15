package com.memariyan.optimizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
@PropertySource(value = {"classpath:message-broker/kafka.properties"})
public class OptimizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OptimizerApplication.class, args);
    }

}
