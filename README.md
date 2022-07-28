# Spring Cloud Stream + AWS/localstack SQS

### IMPORTANT! For now localstack sqs worked for me on 0.12.12 idk why latest doesn't work.

### IMPORTANT! If you want to send `String` data to SQS queue you have to set `spring.cloud.stream.sqs.bindings.functionName-in-0.consumer.snsFanout=false` to false. Otherwise sqs expects some weird? format.

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

### First don't forget to create SQS queues with `terraform apply` (terraform directory) 

### Postman config for Controller testing (PRODUCER) in `docs/Postman`

### Sources:
- https://github.com/Kamivix/RabbitMQ/tree/SQS (thx)
- https://www.youtube.com/watch?v=q3zo3YREfJI
- https://github.com/maciejwalkowiak/spring-cloud-stream-binder-sqs/blob/master/spring-cloud-stream-binder-sqs-samples/simple-producer/src/main/resources/application.yml

_______

### 0. Maven dependency
*pom.xml*
```xml
<dependency>
    <groupId>de.idealo.spring</groupId>
    <artifactId>spring-cloud-stream-binder-sqs</artifactId>
    <version>1.7.1</version>
</dependency>
```

### 4. application.properties

*application.properties*
```properties
# -- Configuration of AWS/localstack --
# https://www.youtube.com/watch?v=q3zo3YREfJI
# https://github.com/maciejwalkowiak/spring-cloud-stream-binder-sqs/blob/master/spring-cloud-stream-binder-sqs-samples/simple-producer/src/main/resources/application.yml
cloud.aws.region.static=eu-central-1
cloud.aws.sqs.region=eu-central-1
cloud.aws.credentials.access-key=foo
cloud.aws.credentials.secret-key=bar
cloud.aws.sqs.endpoint=http://localhost:4566
cloud.aws.stack.auto=false

# -- My namespace --
config.sqs.userTopic=userTopic

config.sqs.articleTopic=articleTopic

config.sqs.commentTopic=commentTopic

# -- Configuration of functions --
spring.cloud.function.definition=userExchange;articleExchange;commentExchange

# -- Queues + function binding --
#snsFanout must be if we want to send/receive String, not sqs some weird format ?
#(pokazuje ze nie istnieje taka konfiguracja, ale dziala :) )
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
