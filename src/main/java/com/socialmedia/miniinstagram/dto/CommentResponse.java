package com.socialmedia.miniinstagram.dto;

import com.socialmedia.miniinstagram.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    private Long id;
    private Long postId;

    private Long authorId;
    private String authorUsername;

    private String text;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment c) {
        CommentResponse dto = new CommentResponse();
        dto.setId(c.getId());
        dto.setPostId(c.getPost().getId());
        dto.setAuthorId(c.getUser().getId());
        dto.setAuthorUsername(c.getUser().getUsername());
        dto.setText(c.getText());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
