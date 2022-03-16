package com.example.kafkademo.prodconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "orders", groupId = "rx")
    void listener(String data) {
        System.out.println("Listener received: " + data + ":)");
    }
}
