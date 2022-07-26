# Spring Cloud Stream + RabbitMQ
Aplikacja składa się z 3 "programów":
- producer - produkuje dane wysyłane do exchange (funkcji)
- processor - exchange (funkcje)
- consumer - konsumuje dane z exchange (przez kolejkę)

Przepływ danych w projekcie:<br/>
`PRODUCER -> in(exchange) PROCESSOR out(exchange) -> in(queue) CONSUMER`

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

    public String sendEmail() {
        // direct cmd to queue
        sendToTopic(allOutTopic, "EMAIL_SENT");
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
By default RabbitMQ creates exchange from registered functions in aplication.properties

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

*application.properties*
```yaml
## Configuration of RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=user
spring.rabbitmq.password=pass

## My RabbitMQ namespace
config.rabbitmq.userExchangeIn=moje.input.user
config.rabbitmq.userExchangeOut=moje.output.user

config.rabbitmq.articleExchangeIn=moje.input.article
config.rabbitmq.articleExchangeOut=moje.output.article

config.rabbitmq.commentExchangeIn=moje.input.comment
config.rabbitmq.commentExchangeOut=moje.output.comment

config.rabbitmq.allOutput=all.out

## Processor Config
# Configuration of functions (exchanges)
spring.cloud.function.definition=convertToUppercase;userExchange;articleExchange;commentExchange;onReceive

# By default exchange is created with name 'functionName-in/out-0'
spring.cloud.stream.bindings.userExchange-in-0.destination=${config.rabbitmq.userExchangeIn}
spring.cloud.stream.bindings.userExchange-out-0.destination=${config.rabbitmq.allOutput}

spring.cloud.stream.bindings.articleExchange-in-0.destination=${config.rabbitmq.articleExchangeIn}
spring.cloud.stream.bindings.articleExchange-out-0.destination=${config.rabbitmq.allOutput}

spring.cloud.stream.bindings.commentExchange-in-0.destination=${config.rabbitmq.commentExchangeIn}
spring.cloud.stream.bindings.commentExchange-out-0.destination=${config.rabbitmq.allOutput}

## Consumer Config
spring.cloud.stream.bindings.onReceive-in-0.destination=${config.rabbitmq.allOutput}
spring.cloud.stream.bindings.onReceive-in-0.group=consumer
```

