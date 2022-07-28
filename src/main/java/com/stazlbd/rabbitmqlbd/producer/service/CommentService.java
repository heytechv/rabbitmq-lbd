package com.stazlbd.rabbitmqlbd.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

@Service
public class CommentService {

    @Autowired private StreamBridge streamBridge;

    @Value("${config.sqs.commentTopic}") public String topic;

    public String createComment() {
        sendToTopic("COMMENT_CREATED");
        return "comment created!";
    }

    public String updateArticle() {
        sendToTopic("COMMENT_UPDATED");
        return "comment updated!";
    }

    public String deleteComment() {
        sendToTopic("COMMENT_DELETED");
        return "comment deleted!";
    }

    private void sendToTopic(Object data) {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send(topic, data);
    }

}
