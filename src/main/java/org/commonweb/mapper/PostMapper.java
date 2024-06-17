package org.commonweb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.commonweb.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {
    Optional<Post> findById(Long id);
    List<Post> findAll();
    List<Post> findByUserId(String userId);
    List<Post> findByTitleContaining(String title);
    List<Post> findByContentContaining(String content);
    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Post> findByTitleOrContent(String keyword);
    void createPost(Post post);
    void updatePost(Post post);
    void deletePostById(Long id);
}
