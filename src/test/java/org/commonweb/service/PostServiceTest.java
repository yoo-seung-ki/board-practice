package org.commonweb.service;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.mapper.PostMapper;
import org.commonweb.mapper.UserMapper;
import org.commonweb.serviceimpl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PostMapper postMapper;

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
    void createPostTest() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("userId");

        // SecurityContext 모킹 및 SecurityContextHolder에 설정
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // UserMapper 모킹 설정
        User user = new User(); // 혹은 User.builder().userId("userId").build(); 등으로 객체 생성
        when(userMapper.findByUserId(anyString())).thenReturn(Optional.of(user));

        // PostCreationRequest 객체 생성 및 초기화
        PostCreationRequest request = new PostCreationRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        // 필요한 경우, 다른 필드도 설정

        // Mockito를 사용하여 postMapper.createPost 메서드가 호출될 때, 예상 결과를 설정
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        // createPost는 void 반환 타입이므로 반환값 설정이 필요 없습니다.
        // 대신 메서드 호출을 검증합니다.
        doNothing().when(postMapper).createPost(any(Post.class));

        // 서비스 메서드 호출
        Post createdPost = postService.createPost(request);

        // 검증
        assertNotNull(createdPost);
        assertEquals(request.getTitle(), createdPost.getTitle());
        assertEquals(request.getContent(), createdPost.getContent());
        verify(postMapper).createPost(any(Post.class)); // 메서드 호출 검증
    }

    @Test
    void findPostByIdTest() {
        when(postMapper.findByUserId("user123")).thenReturn(Collections.singletonList(post));

        List<Post> foundPosts = postService.searchPostsByUserId("user123");
        assertFalse(foundPosts.isEmpty()); // 리스트가 비어있지 않은지 확인
        assertEquals(post, foundPosts.get(0)); // 첫 번째 항목이 예상하는 Post 객체와 동일한지 확인
    }

    @Test
    void getAllPostsTest() {
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postMapper.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(posts.size(), result.size());
    }

    @Test
    void searchPostsByTitleOrContentTest() {
        // Post 객체에 title과 content 필드에 실제 값을 할당
        Post post = Post.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        List<Post> expectedPosts = Collections.singletonList(post);

        String keyword = "Test";
        when(postMapper.findByTitleOrContent(keyword)).thenReturn(expectedPosts);

        List<Post> foundPosts = postService.searchPostsByTitleOrContent(keyword);

        // 검증 부분을 수정합니다. getContent() 메서드 호출을 제거합니다.
        assertTrue(foundPosts.stream().anyMatch(p -> p.getTitle().contains(keyword) || p.getContent().contains(keyword)));
    }
}
