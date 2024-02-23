package org.commonweb.service;


import org.commonweb.entity.Comment;
import org.commonweb.impl.CommentServiceImpl;
import org.commonweb.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveCommentTest() {
        Comment comment = new Comment(); // Comment 객체 초기화
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.saveComment(comment);

        assertEquals(comment, savedComment);
    }

    @Test
    void findCommentsByPostIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(Collections.singletonList(new Comment()));
        when(commentRepository.findByPostId(anyLong(), eq(pageable))).thenReturn(page);

        Page<Comment> result = commentService.findCommentsByPostId(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findCommentsByUserIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(Collections.singletonList(new Comment()));
        when(commentRepository.findByUser_UserId(any(String.class), eq(pageable))).thenReturn(page);

        Page<Comment> result = commentService.findCommentsByUserId("user123", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllCommentsTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> page = new PageImpl<>(Collections.singletonList(new Comment()));
        when(commentRepository.findAll(eq(pageable))).thenReturn(page);

        Page<Comment> result = commentService.getAllComments(pageable);

        assertEquals(1, result.getTotalElements());
    }
}
