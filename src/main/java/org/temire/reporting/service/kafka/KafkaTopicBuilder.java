package org.temire.reporting.service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicBuilder {

    @Value("${kafka.producer.order.name}")
    String topicName;

    @Bean
    public NewTopic createOrderTopic(){
        return TopicBuilder.name(topicName)
                .build();
    }
}
