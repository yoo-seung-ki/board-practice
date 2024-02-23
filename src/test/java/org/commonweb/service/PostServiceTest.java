package org.commonweb.service;

import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.impl.PostServiceImpl;
import org.commonweb.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "user123", "password", "user@example.com", "User Name", "01012345678", LocalDateTime.now(), LocalDateTime.now());
        post = new Post(1L, "Test Title", "Test Content", user, LocalDateTime.now(), LocalDateTime.now());

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePostTest() {
        // Post 객체 생성 및 초기화
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();

        // Mockito를 사용하여 postRepository.save 메서드가 호출될 때 post 객체를 반환하도록 설정
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // 서비스 메서드 호출
        Post savedPost = postService.savePost(post);

        // 검증
        assertNotNull(savedPost);
        assertEquals("Test Title", savedPost.getTitle());
        assertEquals("Test Content", savedPost.getContent());
    }

    @Test
    void findPostByIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Post post = new Post(); // Post 객체 초기화
        Page<Post> page = new PageImpl<>(Collections.singletonList(post));

        when(postRepository.findByUser_UserId("user123",pageable)).thenReturn(page);

        Page<Post> foundPost = postService.searchPostsByUserId("user123",pageable);
        assertEquals(post, foundPost.getContent().get(0));
    }

    @Test
    void getAllPostsTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Post post = new Post(); // Post 객체 초기화
        Page<Post> page = new PageImpl<>(Collections.singletonList(post));

        when(postRepository.findAll(pageable)).thenReturn(page);

        Page<Post> result = postService.getAllPosts(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(post, result.getContent().get(0));
    }

    @Test
    void searchPostsByTitleOrContentTest() {
        Pageable pageable = PageRequest.of(0, 10);
        // Post 객체에 title과 content 필드에 실제 값을 할당
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        Page<Post> page = new PageImpl<>(Collections.singletonList(post));

        String keyword = "Test";
        when(postRepository.findByTitleOrContent(keyword, pageable)).thenReturn(page);
        Page<Post> foundPosts = postService.searchPostsByTitleOrContent(keyword, pageable);
        assertTrue(foundPosts.getContent().stream().anyMatch(p -> p.getTitle().contains(keyword) || p.getContent().contains(keyword)));
    }


}
