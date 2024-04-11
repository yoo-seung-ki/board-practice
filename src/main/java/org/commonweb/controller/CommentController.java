package org.commonweb.controller;

import jakarta.validation.Valid;
import org.commonweb.dto.request.CommentCreationRequest;
import org.commonweb.dto.request.CommentUpdateRequest;
import org.commonweb.dto.response.CommentResponse;
import org.commonweb.entity.Comment;
import org.commonweb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    // Comment 엔티티를 CommentResponse DTO로 변환하는 메서드

    private CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserId(comment.getUser().getUserId());
        response.setPostId(comment.getPost().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }

    // 게시글 ID에 따른 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    // createComment 메서드 예시
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreationRequest request) {
        Comment comment = commentService.createComment(request);
        CommentResponse response = convertToCommentResponse(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        Comment updatedComment = commentService.updateComment(commentId, request);
        CommentResponse response = convertToCommentResponse(updatedComment);
        return ResponseEntity.ok(response);
    }
}
