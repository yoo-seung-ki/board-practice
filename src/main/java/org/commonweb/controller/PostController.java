package org.commonweb.controller;

import org.commonweb.dto.request.PostCreationRequest;
import org.commonweb.dto.request.PostUpdateRequest;
import org.commonweb.dto.response.PostResponse;
import org.commonweb.entity.Post;
import org.commonweb.exception.PostNotFoundException;
import org.commonweb.security.HtmlSanitizer;
import org.commonweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Posts")
public class PostController {
    private final PostService postService;
    private final HtmlSanitizer htmlSanitizer;

    @Autowired
    public PostController(PostService postService, HtmlSanitizer htmlSanitizer) {
        this.postService = postService;
        this.htmlSanitizer = htmlSanitizer;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreationRequest request) {
        request.setTitle(htmlSanitizer.sanitize(request.getTitle()));
        request.setContent(htmlSanitizer.sanitize(request.getContent()));
        Post post = postService.createPost(request);
        PostResponse postResponse = convertToPostResponse(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request) {
        request.setTitle(htmlSanitizer.sanitize(request.getTitle()));
        request.setContent(htmlSanitizer.sanitize(request.getContent()));
        Post updatedPost = postService.updatePost(postId, request);
        PostResponse postResponse = convertToPostResponse(updatedPost);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.searchPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with Id: " + postId));
        PostResponse postResponse = convertToPostResponse(post);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponse> postResponses = posts.stream()
                .map(this::convertToPostResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePostById(postId);
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