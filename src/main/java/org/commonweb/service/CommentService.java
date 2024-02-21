package org.commonweb.service;

import org.commonweb.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment);
    Page<Comment> findCommentsByPostId(Long postId, Pageable pageable);
    Page<Comment> findCommentsByUserId(String userId, Pageable pageable);
    Page<Comment> getAllComments(Pageable pageable);
}
