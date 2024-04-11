package org.commonweb.service;


import org.commonweb.dto.request.CommentCreationRequest;
import org.commonweb.entity.Comment;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.repository.CommentRepository;
import org.commonweb.repository.PostRepository;
import org.commonweb.repository.UserRepository;
import org.commonweb.serviceimpl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCommentTest() {
        // SecurityContext에 대한 모의 설정
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user123"); // 예시로 사용자 ID를 반환하도록 설정
        SecurityContextHolder.setContext(securityContext);

        // Given
        CommentCreationRequest request = new CommentCreationRequest();
        request.setPostId(1L);
        request.setUserId("user123");
        request.setContent("This is a test comment.");

        User mockUser = User.builder()
                .userId("user123")
                .password("password")
                .email("test@example.com")
                .name("Test User")
                .phoneNumber("01012345678")
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .user(mockUser)
                .build();

        Comment expectedComment = Comment.builder()
                .id(1L)
                .post(mockPost)
                .user(mockUser)
                .content("This is a test comment.")
                .build();

        when(commentRepository.save(isA(Comment.class))).thenReturn(expectedComment);
        when(userRepository.findByUserId("user123")).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        // When
        Comment actualComment = commentService.createComment(request);

        // Then
        assertEquals(expectedComment.getContent(), actualComment.getContent());
        assertEquals(expectedComment.getUser().getUserId(), actualComment.getUser().getUserId());
        assertEquals(expectedComment.getPost().getId(), actualComment.getPost().getId());
    }

    @Test
    void findCommentsByPostIdTest() {
        List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(commentRepository.findByPostId(anyLong())).thenReturn(expectedComments);

        List<Comment> result = commentService.findCommentsByPostId(1L);

        assertEquals(expectedComments.size(), result.size());
    }

    @Test
    void findCommentsByUserIdTest() {
        List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(commentRepository.findByUser_UserId(any(String.class))).thenReturn(expectedComments);

        List<Comment> result = commentService.findCommentsByUserId("user123");

        assertEquals(expectedComments.size(), result.size());
        // 추가적인 검증 로직을 적용할 수 있습니다.
    }


    @Test
    void getAllCommentsTest() {
        List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(commentRepository.findAll()).thenReturn(expectedComments);

        List<Comment> result = commentService.getAllComments();

        assertEquals(expectedComments.size(), result.size());
        // 추가적인 검증 로직을 적용할 수 있습니다.
    }
}
