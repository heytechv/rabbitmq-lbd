package com.stazlbd.rabbitmqlbd.rabbitmq.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private StreamBridge streamBridge;

    @Value("${config.rabbitmq.userExchangeIn}") public String topic;
    @Value("${config.rabbitmq.allOutput}") public String allOutTopic;

    public String createUser() {
        sendToTopic("USER_CREATED");
        return "user created!";
    }

    public String sendEmail() {
        // direct cmd to queue
        sendToTopic(allOutTopic, "EMAIL_SENT");
        return "email sent!";
    }

    private void sendToTopic(String topic, Object data) {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send(topic, data);
    }

    private void sendToTopic(Object data) {
        sendToTopic(topic, data);
    }

}
