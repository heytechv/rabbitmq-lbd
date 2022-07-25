package com.stazlbd.rabbitmqlbd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired private StreamBridge streamBridge;


    public String createArticle() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "ARTICLE_CREATED");

        return "article created!";
    }

    public String updateArticle() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "ARTICLE_UPDATED");

        return "article updated!";
    }


}
