package org.commonweb.repository;

import org.commonweb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId); // 특정 게시글 ID로 댓글 리스트 찾기
    List<Comment> searchByPostId(Long postId); // 특정 게시글에 대한 댓글 수 세기
    List<Comment> findByUser_UserId(String userId); // 특정 사용자 ID가 작성한 댓글 리스트 찾기
    // 커스텀 쿼리 예시
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.createdAt >= :startDate AND c.createdAt <= :endDate")
    List<Comment> findCommentsByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
