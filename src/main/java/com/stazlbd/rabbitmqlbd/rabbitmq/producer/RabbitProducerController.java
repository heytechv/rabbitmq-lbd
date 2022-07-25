package com.stazlbd.rabbitmqlbd.rabbitmq.producer;

import com.stazlbd.rabbitmqlbd.rabbitmq.producer.service.ArticleService;
import com.stazlbd.rabbitmqlbd.rabbitmq.producer.service.CommentService;
import com.stazlbd.rabbitmqlbd.rabbitmq.producer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
