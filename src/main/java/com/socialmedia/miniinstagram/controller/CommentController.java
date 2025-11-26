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

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @RequestAttribute("currentUser") User currentUser,
            @RequestBody CommentCreateRequest req) {

        return ResponseEntity.ok(commentService.addComment(postId, currentUser, req));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User currentUser) {

        commentService.deleteComment(id, currentUser);
        return ResponseEntity.ok("Comment deleted");
    }
}
