package com.stazlbd.rabbitmqlbd.producer.service;

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

    /** Direct to queue (consumer) input */
    public String sendEmail() {
        // > each function has own output (take a look at application.properties)
        sendToTopic("onReceive-in-0", "EMAIL_SENT");

        // > or all functions have common output
        // sendToTopic(allOutTopic, "EMAIL_SENT");

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
