package com.stazlbd.rabbitmqlbd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired private StreamBridge streamBridge;

    @Value("${config.rabbitmq.articleExchangeIn}") public String topic;


    public String createArticle() {
        sendToTopic("ARTICLE_CREATED");

        return "article created!";
    }

    public String updateArticle() {
        sendToTopic("ARTICLE_UPDATE");

        return "article updated!";
    }

    private void sendToTopic(Object data) {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send(topic, data);
    }


}
