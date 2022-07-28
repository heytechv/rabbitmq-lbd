# Spring Cloud Stream + AWS/localstack SQS

### docker-compose.yml from Docker Hub (for developers example)

### This project consists of:
- producer - produces data we send to exchange (function)
- consumer - get data from exchange (via queue)

### This project structure:<br/>
`PRODUCER -> in(queue) CONSUMER`
```
    ┌────────────────┐
  ─>│userExchange    │
    └────────────────┘
    ┌────────────────┐
  ─>│articleExchange │ 
    └────────────────┘
    ┌────────────────┐
  ─>│commentExchange │
    └────────────────┘
```

### Postman config for Controller testing (PRODUCER) in `docs/Postman`

### Sources:
- [Apache Kafka + Spring Boot](https://www.confluent.io/blog/apache-kafka-spring-boot-application/)

_______

### 0. Maven dependency
*pom.xml*
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-kafka</artifactId>
    <version>3.2.4</version>
</dependency>
```

### 4. application.properties

*application.properties*
```properties
# -- Configuration of Kafka --
# https://www.youtube.com/watch?v=q3zo3YREfJI
# https://github.com/maciejwalkowiak/spring-cloud-stream-binder-sqs/blob/master/spring-cloud-stream-binder-sqs-samples/simple-producer/src/main/resources/application.yml

spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.consumer.bootstrap-servers=localhost:9092

# -- My namespace --
config.sqs.userTopic=userTopic

config.sqs.articleTopic=articleTopic

config.sqs.commentTopic=commentTopic

# -- Configuration of functions --
spring.cloud.function.definition=userExchange;articleExchange;commentExchange

# -- Queues + function binding --
#snsFanout must be if we want to send/receive String, not sqs some weird format ?
spring.cloud.stream.sqs.bindings.userExchange-in-0.consumer.snsFanout=false
spring.cloud.stream.bindings.userExchange-in-0.destination=${config.sqs.userTopic}
spring.cloud.stream.bindings.userExchange-out-0.destination=${config.sqs.userTopic}

spring.cloud.stream.sqs.bindings.articleExchange-in-0.consumer.snsFanout=false
spring.cloud.stream.bindings.articleExchange-in-0.destination=${config.sqs.articleTopic}
spring.cloud.stream.bindings.articleExchange-out-0.destination=${config.sqs.articleTopic}

spring.cloud.stream.sqs.bindings.commentExchange-in-0.consumer.snsFanout=false
spring.cloud.stream.bindings.commentExchange-in-0.destination=${config.sqs.commentTopic}
spring.cloud.stream.bindings.commentExchange-out-0.destination=${config.sqs.commentTopic}
```
