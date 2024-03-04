package org.commonweb.controller;

import org.commonweb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
}
