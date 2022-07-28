package com.stazlbd.rabbitmqlbd.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private StreamBridge streamBridge;

    @Value("${config.sqs.userTopic}") public String topic;

    public String createUser() {
        sendToTopic("USER_CREATED");
        return "user created!";
    }

    /** Direct to queue (consumer) input */
    public String sendEmail() {
        // todo
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
