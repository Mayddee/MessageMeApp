package org.example.chatgateway.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducers {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopics topics;

    public KafkaProducers(KafkaTemplate<String, String> kafkaTemplate, KafkaTopics topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    public void publishMessageNew(String key, String payload) {
        kafkaTemplate.send(topics.messageNew(), key, payload);
    }
}


