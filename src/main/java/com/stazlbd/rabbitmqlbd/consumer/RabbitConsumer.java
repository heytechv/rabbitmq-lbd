package com.stazlbd.rabbitmqlbd.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class RabbitConsumer {

    public final Logger log = LoggerFactory.getLogger(RabbitConsumer.class);

    /** User Exchange */
    @Bean public Consumer<String> userExchange() {
        return message -> {
            log.info("[U] Received '{}'", message);
        };
    }

    /** Article Exchange */
    @Bean public Consumer<String> articleExchange() {
        return message -> {
            log.info("[A] Received '{}'", message);
        };
    }

    /** Comment Exchange */
    @Bean public Consumer<String> commentExchange() {
        return message -> {
            log.info("[C] Received {}", message);
        };
    }

}
