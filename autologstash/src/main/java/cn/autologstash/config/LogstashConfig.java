package cn.autologstash.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix="db2es.autologstash")
@Data
public class LogstashConfig {
    private String kafkaServer;
    private String kafkaTopic;
    private String logstashPath;
    private String consumerThreads;
    private String elasticsearchServer;
    private String logstashConfPath;
}
