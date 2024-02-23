package org.commonweb.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.commonweb.entity.Post;
import org.commonweb.repository.PostRepository;
import org.commonweb.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    /*@Override
    public Post findPostById(Long id, Pageable pageable) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
    }*/

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> searchPostsByUserId(String userId, Pageable pageable) {
        return postRepository.findByUser_UserId(userId, pageable);
    }

    @Override
    public Page<Post> searchPostsByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public Page<Post> searchPostsByContent(String content, Pageable pageable) {
        return postRepository.findByContentContaining(content, pageable);
    }

    @Override
    public Page<Post> searchPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return postRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<Post> searchPostsByTitleOrContent(String keyword, Pageable pageable) {
        return postRepository.findByTitleOrContent(keyword, pageable);
    }
}
