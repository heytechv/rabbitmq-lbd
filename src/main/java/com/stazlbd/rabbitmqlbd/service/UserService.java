package com.stazlbd.rabbitmqlbd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private StreamBridge streamBridge;

    @Value("${config.rabbitmq.userExchangeIn}") public String topic;


    public String createUser() {
        sendToTopic("USER_CREATED");

        return "user created!";
    }

    private void sendToTopic(Object data) {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send(topic, data);
    }


}
