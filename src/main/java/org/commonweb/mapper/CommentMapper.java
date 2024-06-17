package org.commonweb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.commonweb.entity.Comment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    List<Comment> findCommentsByPostId(Long postId);
    List<Comment> findCommentsByUserId(String userId);
    List<Comment> getAllComments();
    void createComment(Comment comment);
    void updateComment(Comment comment);
    Optional<Comment> findById(Long id);
}
