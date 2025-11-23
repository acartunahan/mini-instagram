package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.dto.PostCreateRequest;
import com.socialmedia.miniinstagram.dto.PostResponse;
import com.socialmedia.miniinstagram.dto.PostUpdateRequest;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.service.PostService;
import com.socialmedia.miniinstagram.service.LikeService;   // 🔹 EKLENDİ
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

    // POST /api/posts → Post oluşturma (resim + açıklama)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody PostCreateRequest req) {

        Post post = postService.createPost(currentUser, req);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // GET /api/posts → Tüm postlar (feed)
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAll();
        List<PostResponse> response = posts.stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // GET /api/posts/{id} → Tekil post detay
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        Post post = postService.getById(id);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // PUT /api/posts/{id} → Sadece owner veya ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody PostUpdateRequest req) {

        Post updated = postService.updatePost(id, currentUser, req);
        return ResponseEntity.ok(PostResponse.from(updated));
    }

    // DELETE /api/posts/{id} → Sadece owner veya ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        postService.deletePost(id, currentUser);
        return ResponseEntity.ok("Post deleted");
    }

    // POST /api/posts/{id}/view → görüntülenme sayacını artır
    @PostMapping("/{id}/view")
    public ResponseEntity<?> incrementView(@PathVariable Long id) {
        postService.incrementViewCount(id);
        return ResponseEntity.ok("View count increased");
    }

    // POST /api/posts/{id}/likes → Postu beğenme (kullanıcı başına tek beğeni)
    @PostMapping("/{id}/likes")
    public ResponseEntity<?> likePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        int likeCount = likeService.likePost(id, currentUser);
        return ResponseEntity.ok("Post liked. Current like count = " + likeCount);
    }

    // DELETE /api/posts/{id}/likes → Beğeniyi geri alma
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> unlikePost(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        int likeCount = likeService.unlikePost(id, currentUser);
        return ResponseEntity.ok("Like removed. Current like count = " + likeCount);
    }
}
