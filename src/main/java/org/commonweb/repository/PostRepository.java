package org.commonweb.repository;

import org.commonweb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser_UserId(String userId); // 특정 사용자 ID로 게시글 리스트 찾기
    List<Post> findByTitleContaining(String title); // 제목에 특정 키워드가 포함된 게시글 찾기
    List<Post> findByContentContaining(String content);
    // List<Post> findByTagsContaining(String tag);
    // tag 는 지금 단계에서는 고려하지 않는 시스템이므로 제외해놓았음
    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // JPQL을 사용한 복잡한 쿼리 예시
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> findByTitleOrContent(@Param("keyword") String keyword);
}
