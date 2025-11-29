package org.example.chatgateway.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic deliveryTopic(@Value("${topics.delivery}") String name) {
        return new NewTopic(name, 6, (short) 1);
    }

    @Bean
    public NewTopic messageNewTopic(@Value("${topics.messageNew}") String name) {
        return new NewTopic(name, 6, (short) 1);
    }
}
