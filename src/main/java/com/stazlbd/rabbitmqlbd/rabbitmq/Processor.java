package com.stazlbd.rabbitmqlbd.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class Processor {

    public final Logger log = LoggerFactory.getLogger(Processor.class);


    /** Demo */
    @Bean public Function<String, String> convertToUppercase() {
        return (value) -> {
            log.info("Received {}", value);
            String upperCaseValue = value.toUpperCase();
            log.info("Sending {}", upperCaseValue);
            return upperCaseValue;
        };
    }

    /**  */

}
