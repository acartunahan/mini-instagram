package com.socialmedia.miniinstagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserProfile {
    private Long id;
    private String username;
    private int postCount;
    private LocalDateTime joinedAt;
}
