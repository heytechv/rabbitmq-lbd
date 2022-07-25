package com.stazlbd.rabbitmqlbd.controller;

import com.stazlbd.rabbitmqlbd.service.ArticleService;
import com.stazlbd.rabbitmqlbd.service.CommentService;
import com.stazlbd.rabbitmqlbd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

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
