package org.commonweb.service;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostService {

    public void deletePostById(Long id);
    Page<Post> getAllPosts(Pageable pageable);
    Page<Post> searchPostsByUserId(String userId, Pageable pageable);
    public Optional<Post> searchPostById(Long id);
    Page<Post> searchPostsByTitle(String title, Pageable pageable);
    Page<Post> searchPostsByContent(String content, Pageable pageable);
    Page<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Post> searchPostsByTitleOrContent(String keyword, Pageable pageable);
    public Post createPost(PostCreationRequest request);
    public Post updatePost(Long postId, PostUpdateRequest request);
}
