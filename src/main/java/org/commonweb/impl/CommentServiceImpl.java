package org.commonweb.impl;

import jakarta.transaction.Transactional;
import org.commonweb.dto.request.CommentCreationRequest;
import org.commonweb.dto.request.CommentUpdateRequest;
import org.commonweb.entity.Comment;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.exception.CommentNotFoundException;
import org.commonweb.exception.PostNotFoundException;
import org.commonweb.exception.UserNotFoundException;
import org.commonweb.repository.CommentRepository;
import org.commonweb.repository.PostRepository;
import org.commonweb.repository.UserRepository;
import org.commonweb.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + request.getPostId()));
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        // Comment 객체 생성 및 반환
        return commentRepository.save(Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build());
    }

    @Override
    public Page<Comment> findCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable);
    }

    @Override
    public Page<Comment> findCommentsByUserId(String userId, Pageable pageable) {
        return commentRepository.findByUser_UserId(userId, pageable);
    }
    @Override
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment updateComment(Long commentId, CommentUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // 사용자 ID 또는 사용자명을 가져옵니다.

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // 사용자 확인
        if (!comment.getUser().getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You do not have permission to modify this comment");
        }

        comment.updateContent(request.getContent()); // 엔티티 내부의 업데이트 메서드를 호출
        return commentRepository.save(comment); // 변경된 엔티티 저장
    }
}
