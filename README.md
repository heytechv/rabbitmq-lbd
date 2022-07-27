# Spring Cloud Stream + RabbitMQ

### In Spring Cloud Stream application we have 3 function types:
([Spring Cloud Stream example](http://shaikezam.com/spring_cloud_stream_functional))
#### 1. Supplier< Object > as Producer/Publisher
Returns data on output (only output exchange is created)
```java
@Component
static class Producer {
    @Bean public Supplier<String> publish() {
        return "Hello World!";
    }
}
```

In most cases we should use `StreamBridge` instead of `Supplier` as follows:
```java
streamBridge.send("topic.name.i.mean.function", "data");
```

#### 2. Function< Object, Object > as Processor
Seizes data from input and returns on output (input and output exchanges are created)

```java
@Component
static class Processor {
    @Bean public Function<String, String> userExchange() {
        return message -> {
            return messageForQueue.toLowerCase();
        };
    }
}
```

#### 3. Consumer< Object >
Seizes data on input (only input exchange is created)

```java
@Component
public class Consumer {
    @Bean public Consumer<String> onReceive() {
        return message -> log.info("Received the value '{}' in Consumer", message);
    }
}

```

________

### This project consists of:
- producer - produces data we send to exchange (function)
- processor - exchange (function)
- consumer - get data from exchange (via queue?) // todo

### This project structure:<br/>
`PRODUCER -> in(exchange) PROCESSOR out(exchange) -> in(queue) CONSUMER`
```
    ┌────────────────┐
  ─>│userExchange    │─┐
    └────────────────┘ │  
    ┌────────────────┐ └─>┌──────────┐    
  ─>│articleExchange │───>│onReceive │ 
    └────────────────┘ ┌─>└──────────┘
    ┌────────────────┐ │
  ─>│commentExchange │─┘
    └────────────────┘
```


### Postman config for Controller testing (PRODUCER) in `docs/Postman`

### Sources:
- [Spring Cloud Stream using RabbitMQ](https://github.com/smoothed9/spring-cloud-stream-rabbit)
- [Kafka streams with Spring Cloud Stream + Serializer](https://piotrminkowski.com/2021/11/11/kafka-streams-with-spring-cloud-stream/)
- [Spring Cloud Stream example](http://shaikezam.com/spring_cloud_stream_functional)
- [Official spring-cloud-stream-binder-rabbit](https://github.com/spring-cloud/spring-cloud-stream-binder-rabbit)
- [Messaging RabbitMQ Tutorial (OK)](https://www.javainuse.com/messaging/rabbitmq/exchange?fbclid=IwAR0Z6YmPE6tl7awKltyukuKpRAclIzsnPNDXXox_AgBiGmeX8D7qj63vw1M)
- [RabbitMQ docs](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)
- [Spring Cloud Stream Tutorial](https://www.youtube.com/watch?v=YEci46QRJ7E)
- [Spring Cloud Stream Tutorial RabbitMQ](https://www.youtube.com/watch?v=Y1bwOL08mqs)
- [Spring Boot RabbitMQ](https://www.youtube.com/watch?v=o4qCdBR4gUM)

_______

### 0. Maven dependency
*pom.xml*
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
    <version>3.2.4</version>
</dependency>
```

### 1. Producer
Producer is just a standard Controller, that receives HTTP requests and send data to exchange or queue.

*RabbitProducerController.java* 
```java
@RestController
public class RabbitProducerController {

    @Autowired private UserService userService;
    @Autowired private ArticleService articleService;
    @Autowired private CommentService commentService;

    /** User */
    @PostMapping("/user")
    public String createUser() {
        return userService.createUser();
    }

    /** Article */
    @PostMapping("/article")
    public String createArticle() {
        return articleService.createArticle();
    }

    @PutMapping("/article")
    public String updateArticle() {
        return articleService.updateArticle();
    }

    /** Comment */
    @PostMapping("/comment")
    public String createComment() {
        return commentService.createComment();
    }

    @PutMapping("/comment")
    public String updateComment() {
        return commentService.updateArticle();
    }

    @DeleteMapping("/comment")
    public String deleteComment() {
        return commentService.deleteComment();
    }

    /** Command */
    @PostMapping("/emailToUser")
    public String sendEmailToUser() {
        return userService.sendEmail();
    }
    
}
```

*UserService.java, ArticleService.java, CommentService.java just send data to exhange or queue*
```java
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
```

### 3. Processor
By default RabbitMQ creates exchanges from registered functions in aplication.properties

*RabbitProcessor.java*
```java
@Component
public class RabbitProcessor {

    public final Logger log = LoggerFactory.getLogger(RabbitProcessor.class);
    
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
```

### 3. Consumer

*RabbitConsumer.java*
```java
@Component
public class RabbitConsumer {

    public final Logger log = LoggerFactory.getLogger(RabbitConsumer.class);

    @Bean public Consumer<String> onReceive() {
        return message -> {
            log.info("Received the value '{}' in Consumer", message);
        };
    }

}
```

### 4. Register in application.properties
We can bind each exchange (Function) to its own output<br/>
<b>or</b> we can bind every exchange (Function) output to one common output (<b>idk if optimal</b>)<br/>
(two config possibilities are shown below)

*application.properties*
```properties
# -- Configuration of RabbitMQ --
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=user
spring.rabbitmq.password=pass

# -- My RabbitMQ namespace --
#config.rabbitmq.userExchangeIn=user.input
config.rabbitmq.userExchangeIn=user.in
config.rabbitmq.userExchangeOut=user.out

config.rabbitmq.articleExchangeIn=article.in
config.rabbitmq.articleExchangeOut=article.out

config.rabbitmq.commentExchangeIn=comment.in
config.rabbitmq.commentExchangeOut=comment.out

config.rabbitmq.allOutput=all.out.queue

# -- Configuration of functions (exchanges) --
spring.cloud.function.definition=userExchange;articleExchange;commentExchange;onReceive

# -- Processor --
# Domyslnie tworzone sa kolejki o nazwie 'nazwaFunkcji-in/out-0'
# mozemy nazwy i konfiguracje overridowac
#
# spring.cloud.stream.bindings.userExchange-in-0.group=nazwa kolejki (queue)

## userExchange
# > One own output
spring.cloud.stream.bindings.userExchange-in-0.destination=${config.rabbitmq.userExchangeIn}
spring.cloud.stream.bindings.userExchange-out-0.destination=${config.rabbitmq.userExchangeOut}
    # nazwa kolejki dla input
spring.cloud.stream.bindings.userExchange-in-0.group=queue

# > or common output
#spring.cloud.stream.bindings.userExchange-in-0.destination=${config.rabbitmq.userExchangeIn}
#spring.cloud.stream.bindings.userExchange-out-0.destination=${config.rabbitmq.allOutput}

## articleExchange
spring.cloud.stream.bindings.articleExchange-in-0.destination=${config.rabbitmq.articleExchangeIn}
spring.cloud.stream.bindings.articleExchange-out-0.destination=${config.rabbitmq.articleExchangeOut}
spring.cloud.stream.bindings.commentExchange-in-0.group=queue

## commentExchange
spring.cloud.stream.bindings.commentExchange-in-0.destination=${config.rabbitmq.commentExchangeIn}
spring.cloud.stream.bindings.commentExchange-out-0.destination=${config.rabbitmq.commentExchangeOut}
spring.cloud.stream.bindings.articleExchange-in-0.group=queue

# -- Consumer --
# > seizes data from each output
spring.cloud.stream.bindings.onReceive-in-0.destination=${config.rabbitmq.userExchangeOut},${config.rabbitmq.articleExchangeOut},${config.rabbitmq.commentExchangeOut}
    # queue name
spring.cloud.stream.bindings.onReceive-in-0.group=${config.rabbitmq.allOutput}
    #
spring.cloud.stream.rabbit.bindings.onReceive-in-0.consumer.bind-queue=true
    # zeby nie tworzyl wielu tylko jedna
spring.cloud.stream.rabbit.bindings.onReceive-in-0.consumer.queueNameGroupOnly=true

# > lub jak bylo wysylane do jednego to z jednego
#spring.cloud.stream.bindings.onReceive-in-0.destination=${config.rabbitmq.allOutput}
#spring.cloud.stream.bindings.onReceive-in-0.group=consumer

```
