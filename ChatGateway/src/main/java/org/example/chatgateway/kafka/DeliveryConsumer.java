package org.example.chatgateway.kafka;


import org.example.chatgateway.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaTopics topics;

    public DeliveryConsumer(SimpMessagingTemplate messagingTemplate, KafkaTopics topics) {
        this.messagingTemplate = messagingTemplate;
        this.topics = topics;
    }

    @KafkaListener(topics = "#{kafkaTopics.delivery()}", groupId = "chat-gateway")
    public void onDelivery(String json) {
        var node = JsonUtil.parse(json);
        String toUserId = node.path("toUserId").asText();
        messagingTemplate.convertAndSendToUser(toUserId, "/queue/messages", json);
    }
}
