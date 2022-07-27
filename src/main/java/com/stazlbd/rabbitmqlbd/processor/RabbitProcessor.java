package com.stazlbd.rabbitmqlbd.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RabbitProcessor {

    public final Logger log = LoggerFactory.getLogger(RabbitProcessor.class);

    /** Demo
     * RabbitMQ domyslnie tworzy exchange z zarejestrowanej funkcji w aplication.properties
     * Tworzy exchange typu 'topic' */
    @Bean public Function<String, String> convertToUppercase() {
        return (value) -> {
            log.info("Received '{}'", value);
            String upperCaseValue = value.toUpperCase();
            log.info("Sending '{}'", upperCaseValue);
            return upperCaseValue;
        };
    }

    /** User Exchange */
    @Bean public Function<String, String> userExchange() {
        return message -> {
            log.info("[U] Received '{}'", message);
            String messageForQueue = message.toLowerCase() + " OK";
            log.info("[U] Sending '{}'", messageForQueue);
            return messageForQueue;
        };
    }

    /** Article Exchange */
    @Bean public Function<String, String> articleExchange() {
        return message -> {
            log.info("[A] Received '{}'", message);
            String messageForQueue = message.toLowerCase() + " OK";
            log.info("[A] Sending '{}'", messageForQueue);
            return messageForQueue;
        };
    }

    /** Comment Exchange */
    @Bean public Function<String, String> commentExchange() {
        return message -> {
            log.info("[C] Received {}", message);
            String messageForQueue = message.toLowerCase() + " OK";
            log.info("[C] Sending {}", messageForQueue);
            return messageForQueue;
        };
    }

}
