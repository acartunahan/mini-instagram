package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.CommentCreateRequest;
import com.socialmedia.miniinstagram.dto.CommentResponse;
import com.socialmedia.miniinstagram.entity.Comment;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.CommentRepository;
import com.socialmedia.miniinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // POST /api/posts/{id}/comments
    public CommentResponse addComment(Long postId, User currentUser, CommentCreateRequest req) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setText(req.getText());

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    // GET /api/posts/{id}/comments
    public List<CommentResponse> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtAsc(post);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    // DELETE /api/comments/{id}
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        boolean isAuthor = comment.getUser().getId().equals(currentUser.getId());
        boolean isPostOwner = comment.getPost().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!(isAuthor || isPostOwner || isAdmin)) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
