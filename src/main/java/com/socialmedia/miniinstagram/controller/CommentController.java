package com.socialmedia.miniinstagram.controller;

import com.socialmedia.miniinstagram.dto.CommentCreateRequest;
import com.socialmedia.miniinstagram.dto.CommentResponse;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // POST /api/posts/{id}/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody CommentCreateRequest req) {

        CommentResponse resp = commentService.addComment(postId, currentUser, req);
        return ResponseEntity.ok(resp);
    }

    // GET /api/posts/{id}/comments
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId) {

        List<CommentResponse> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    // DELETE /api/comments/{id}
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        commentService.deleteComment(id, currentUser);
        return ResponseEntity.ok("Comment deleted");
    }
}
