package org.commonweb.serviceimpl;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.exception.ResourceNotFoundException;
import org.commonweb.mapper.CommentMapper;
import org.commonweb.mapper.PostMapper;
import org.commonweb.mapper.UserMapper;
import org.commonweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostServiceImpl(UserMapper userMapper, PostMapper postMapper, CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public void deletePostById(Long id) {
        if (postMapper.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postMapper.deletePostById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postMapper.findAll();
    }

    @Override
    public List<Post> searchPostsByUserId(String userId) {
        return postMapper.findByUserId(userId);
    }

    @Override
    public Optional<Post> searchPostById(Long id) {
        return postMapper.findById(id);
    }

    @Override
    public List<Post> searchPostsByTitle(String title) {
        return postMapper.findByTitleContaining(title);
    }

    @Override
    public List<Post> searchPostsByContent(String content) {
        return postMapper.findByContentContaining(content);
    }

    @Override
    public List<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postMapper.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Post> searchPostsByTitleOrContent(String keyword) {
        return postMapper.findByTitleOrContent(keyword);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return userMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }

    @Override
    public Post createPost(PostCreationRequest request) {
        User currentUser = getCurrentUser();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postMapper.createPost(post);
        return post;
    }

    @Override
    public Post updatePost(Long postId, PostUpdateRequest request) {
        User currentUser = getCurrentUser();

        Post post = postMapper.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You do not have permission to modify this post");
        }

        post.updateInfo(request.getTitle(), request.getContent());
        postMapper.updatePost(post);
        return post;
    }
}
