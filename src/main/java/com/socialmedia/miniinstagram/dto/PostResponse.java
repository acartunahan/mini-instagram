package com.socialmedia.miniinstagram.dto;

import com.socialmedia.miniinstagram.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponse {

    private Long id;
    private String imageUrl;
    private String description;

    private Long authorId;
    private String authorUsername;

    private int likeCount;
    private int viewCount;

    private int commentCount;
    private List<CommentResponse> comments;

    private LocalDateTime createdAt;

    public static PostResponse from(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setImageUrl(post.getImageUrl());
        dto.setDescription(post.getDescription());
        dto.setAuthorId(post.getUser().getId());
        dto.setAuthorUsername(post.getUser().getUsername());
        dto.setLikeCount(post.getLikeCount());
        dto.setViewCount(post.getViewCount());
        dto.setCreatedAt(post.getCreatedAt());

        if (post.getComments() != null) {
            dto.setCommentCount(post.getComments().size());
            dto.setComments(
                    post.getComments().stream()
                            .map(CommentResponse::from)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setCommentCount(0);
            dto.setComments(List.of());
        }

        return dto;
    }
}
