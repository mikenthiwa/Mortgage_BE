package com.example.mortgage.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MortgageApplicationConsumer {
    @KafkaListener(topics = "mortgage-applications", groupId = "mortgage-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
