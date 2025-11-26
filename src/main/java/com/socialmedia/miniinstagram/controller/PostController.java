package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.dto.PostCreateRequest;
import com.socialmedia.miniinstagram.dto.PostResponse;
import com.socialmedia.miniinstagram.dto.PostUpdateRequest;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.service.PostService;
import com.socialmedia.miniinstagram.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody PostCreateRequest req) {

        Post post = postService.createPost(currentUser, req);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAll();
        return ResponseEntity.ok(posts.stream().map(PostResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(PostResponse.from(postService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody PostUpdateRequest req) {

        return ResponseEntity.ok(PostResponse.from(postService.updatePost(id, currentUser, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        postService.deletePost(id, currentUser);
        return ResponseEntity.ok("Post deleted");
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<?> incrementView(@PathVariable Long id) {
        postService.incrementViewCount(id);
        return ResponseEntity.ok("View count increased");
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> likePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        int count = likeService.likePost(id, currentUser);
        return ResponseEntity.ok("Post liked. Current like count = " + count);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> unlikePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        int count = likeService.unlikePost(id, currentUser);
        return ResponseEntity.ok("Like removed. Current like count = " + count);
    }
}
