package com.stazlbd.rabbitmqlbd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired private StreamBridge streamBridge;


    public String createComment() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "COMMENT_CREATED");

        return "comment created!";
    }

    public String updateArticle() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "COMMENT_UPDATED");

        return "comment updated!";
    }

    public String deleteComment() {
        // streamBridge.send("topic.name.i.mean.function", "data")
        streamBridge.send("convertToUppercase-in-0", "COMMENT_DELETED");

        return "comment deleted!";
    }


}
