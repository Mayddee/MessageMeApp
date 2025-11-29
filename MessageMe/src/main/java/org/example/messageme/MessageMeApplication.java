package org.example.messageme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MessageMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageMeApplication.class, args);
    }

}
