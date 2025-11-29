package org.example.chatgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ChatGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGatewayApplication.class, args);
    }

}
