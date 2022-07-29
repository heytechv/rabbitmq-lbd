package com.stazlbd.rabbitmqlbd.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
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

    @Bean public Consumer<String> robReceiver() {
        return message -> {
            log.info("ROB '{}' MOJE", message);
//            throw new AmqpRejectAndDontRequeueException("failed");
        };
    }

}
