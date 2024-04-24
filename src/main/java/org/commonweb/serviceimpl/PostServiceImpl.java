package org.commonweb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.entity.Post;
import org.commonweb.entity.User;
import org.commonweb.repository.PostRepository;
import org.commonweb.repository.UserRepository;
import org.commonweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }


    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> searchPostsByUserId(String userId) {
        return postRepository.findByUser_UserId(userId);
    }

    public Optional<Post> searchPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Override
    public List<Post> searchPostsByContent(String content) {
        return postRepository.findByContentContaining(content);
    }

    @Override
    public List<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Post> searchPostsByTitleOrContent(String keyword) {
        return postRepository.findByTitleOrContent(keyword);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 여기서는 username이 userId를 의미한다고 가정

        // UserRepository의 메서드를 사용하여 userId로 사용자 조회

        /* User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
           return currentUser; */

        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }

    public Post createPost(PostCreationRequest request) {
        User currentUser = getCurrentUser(); // 현재 로그인한 사용자 정보 조회

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(currentUser) // 게시글의 작성자로 현재 사용자 설정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();
        User currentUser = userRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUserId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this post");
        }

        post.updateInfo(request.getTitle(), request.getContent(), LocalDateTime.now());
        return postRepository.save(post);
    }

}
