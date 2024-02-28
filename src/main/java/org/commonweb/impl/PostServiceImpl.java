package org.commonweb.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;
import org.commonweb.repository.PostRepository;
import org.commonweb.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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

    public Post createPost(PostCreationRequest request) {
        Post post = Post.builder()
                .id(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .user(request.getUser())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
        // 필요한 경우, 추가적인 필드 설정
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
