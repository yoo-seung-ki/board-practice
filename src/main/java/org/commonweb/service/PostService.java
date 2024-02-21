package org.commonweb.service;

import org.commonweb.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    Post savePost(Post post);
    public void deletePostById(Long id);
    /*Post findPostById(Long id, Pageable pageable);*/
    Page<Post> getAllPosts(Pageable pageable);

    Page<Post> searchPostsByUserId(String userId, Pageable pageable);
    Page<Post> searchPostsByTitle(String title, Pageable pageable);
    Page<Post> searchPostsByContent(String content, Pageable pageable);
    Page<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Post> searchPostsByTitleOrContent(String keyword, Pageable pageable);
}
