package com.stazlbd.rabbitmqlbd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private StreamBridge streamBridge;


    public String createUser() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "USER_CREATED");

        return "user created!";
    }


}
