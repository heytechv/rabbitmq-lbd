package com.stazlbd.rabbitmqlbd.rabbitmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class RabbitConsumer {

    public final Logger log = LoggerFactory.getLogger(RabbitConsumer.class);


    @Bean public Consumer<String> onReceive() {
        return message -> {
            log.info("Received the value '{}' in Consumer", message);
        };
    }

}
