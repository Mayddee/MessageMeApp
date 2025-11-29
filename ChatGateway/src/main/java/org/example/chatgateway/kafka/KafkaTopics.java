package org.example.chatgateway.kafka;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopics {

    private final String delivery;
    private final String messageNew;

    public KafkaTopics(
            @Value("${topics.delivery}") String delivery,
            @Value("${topics.messageNew}") String messageNew
    ) {
        this.delivery = delivery;
        this.messageNew = messageNew;
    }

    public String delivery() {
        return delivery;
    }

    public String messageNew() {
        return messageNew;
    }
}

