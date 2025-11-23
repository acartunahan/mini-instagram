package com.socialmedia.miniinstagram.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
}
