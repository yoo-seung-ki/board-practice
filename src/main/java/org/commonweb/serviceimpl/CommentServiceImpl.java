package org.commonweb.serviceimpl;

import org.commonweb.dto.request.CommentCreationRequest;
import org.commonweb.dto.request.CommentUpdateRequest;
import org.commonweb.entity.Comment;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.exception.CommentNotFoundException;
import org.commonweb.exception.PostNotFoundException;
import org.commonweb.exception.UserNotFoundException;
import org.commonweb.mapper.CommentMapper;
import org.commonweb.mapper.PostMapper;
import org.commonweb.mapper.UserMapper;
import org.commonweb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(UserMapper userMapper, PostMapper postMapper, CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public Comment createComment(CommentCreationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // 현재 사용자의 ID 또는 사용자명을 가져옵니다.

        // 요청된 사용자 ID와 현재 인증된 사용자의 ID가 일치하는지 검증합니다.
        if (!request.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not authorized to create comments for other users");
        }

        // 관련 게시글과 사용자 엔티티 조회 로직
        Post post = postMapper.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + request.getPostId()));
        User user = userMapper.findByUserId(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        // Comment 객체 생성 및 반환
        // Comment 객체 생성 및 저장
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        commentMapper.createComment(comment);
        return comment;
    }

    @Override
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentMapper.findCommentsByPostId(postId);
    }

    @Override
    public List<Comment> findCommentsByUserId(String userId) {
        return commentMapper.findCommentsByUserId(userId);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentMapper.getAllComments();
    }

    @Override
    public Comment updateComment(Long commentId, CommentUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // 사용자 ID 또는 사용자명을 가져옵니다.

        Comment comment = commentMapper.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // 사용자 확인
        if (!comment.getUser().getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this comment");
        }

        comment.updateContent(request.getContent()); // 엔티티 내부의 업데이트 메서드를 호출
        comment.setUpdatedAt(LocalDateTime.now());
        commentMapper.updateComment(comment); // 변경된 엔티티 저장
        return comment;
    }
}
