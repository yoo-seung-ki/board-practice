package org.commonweb.controller;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.dto.response.PostResponse;
import org.commonweb.entity.Post;
import org.commonweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreationRequest request) {
        Post post = postService.createPost(request);
        PostResponse postResponse = convertToPostResponse(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postService.searchPostById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with Id: " + id));
        PostResponse postResponse = convertToPostResponse(post);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable) {
        Page<Post> posts = postService.getAllPosts(pageable);
        Page<PostResponse> postResponses = posts.map(this::convertToPostResponse);
        return ResponseEntity.ok(postResponses);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request) {
        Post updatedPost = postService.updatePost(postId, request);
        PostResponse postResponse = convertToPostResponse(updatedPost);
        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long Id) {
        postService.deletePostById(Id);
        return ResponseEntity.noContent().build();
    }

    private PostResponse convertToPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());
        // 필요에 따라 추가 정보를 설정할 수 있습니다.
        return postResponse;
    }
}