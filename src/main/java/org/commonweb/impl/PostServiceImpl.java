package org.commonweb.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.repository.PostRepository;
import org.commonweb.repository.UserRepository;
import org.commonweb.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }


    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> searchPostsByUserId(String userId, Pageable pageable) {
        return postRepository.findByUser_UserId(userId, pageable);
    }

    public Optional<Post> searchPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Page<Post> searchPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Post> searchPostsByContent(String content, Pageable pageable) {
        return postRepository.findByContentContaining(content, pageable);
    }

    @Override
    public Page<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<Post> searchPostsByTitleOrContent(String keyword, Pageable pageable) {
        return postRepository.findByTitleOrContent(keyword, pageable);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 여기서는 username이 userId를 의미한다고 가정

        // UserRepository의 메서드를 사용하여 userId로 사용자 조회

        /* User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
           return currentUser; */

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }

    public Post createPost(PostCreationRequest request) {
        User currentUser = getCurrentUser(); // 현재 로그인한 사용자 정보 조회

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(currentUser) // 게시글의 작성자로 현재 사용자 설정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    public Post updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        post.updateInfo(request.getTitle(), request.getContent(), request.getUpdatedAt());
        // post.setUpdatedAt(LocalDateTime.now()); // 필요하다면 업데이트 시간 설정
        return postRepository.save(post);
    }

}
