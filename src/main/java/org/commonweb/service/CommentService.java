package org.commonweb.service;

import org.commonweb.dto.request.CommentCreationRequest;
import org.commonweb.dto.request.CommentUpdateRequest;
import org.commonweb.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(CommentCreationRequest comment);
    List<Comment> findCommentsByPostId(Long postId);
    List<Comment> findCommentsByUserId(String userId);
    List<Comment> getAllComments();
    Comment updateComment(Long commentId, CommentUpdateRequest request);
}
