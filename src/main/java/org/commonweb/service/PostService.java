package org.commonweb.service;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostService {

    public void deletePostById(Long id);
    List<Post> getAllPosts();
    List<Post> searchPostsByUserId(String userId);
    public Optional<Post> searchPostById(Long id);
    List<Post> searchPostsByTitle(String title);
    List<Post> searchPostsByContent(String content);
    List<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Post> searchPostsByTitleOrContent(String keyword);
    public Post createPost(PostCreationRequest request);
    public Post updatePost(Long postId, PostUpdateRequest request);

}
