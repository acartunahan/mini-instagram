package com.socialmedia.miniinstagram.service;

import com.socialmedia.miniinstagram.dto.CommentCreateRequest;
import com.socialmedia.miniinstagram.dto.CommentResponse;
import com.socialmedia.miniinstagram.entity.Comment;
import com.socialmedia.miniinstagram.entity.Post;
import com.socialmedia.miniinstagram.entity.User;
import com.socialmedia.miniinstagram.exception.AppException;
import com.socialmedia.miniinstagram.model.Role;
import com.socialmedia.miniinstagram.repository.CommentRepository;
import com.socialmedia.miniinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse addComment(Long postId, User currentUser, CommentCreateRequest req) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException("Post not found", HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setText(req.getText());

        return CommentResponse.from(commentRepository.save(comment));
    }

    public List<CommentResponse> getCommentsForPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException("Post not found", HttpStatus.NOT_FOUND));

        return commentRepository.findByPostOrderByCreatedAtAsc(post)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    public void deleteComment(Long commentId, User currentUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException("Comment not found", HttpStatus.NOT_FOUND));

        boolean isAuthor = comment.getUser().getId().equals(currentUser.getId());
        boolean isPostOwner = comment.getPost().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!(isAuthor || isPostOwner || isAdmin)) {
            throw new AppException("You are not allowed to delete this comment", HttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }
}
