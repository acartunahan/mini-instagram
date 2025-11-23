package com.socialmedia.miniinstagram.dto;

import lombok.Data;

@Data
public class PostCreateRequest {
    private String imageUrl;
    private String description;
}
