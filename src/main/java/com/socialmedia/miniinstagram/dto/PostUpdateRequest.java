package com.socialmedia.miniinstagram.dto;

import lombok.Data;

@Data
public class PostUpdateRequest {
    private String imageUrl;
    private String description;
}
