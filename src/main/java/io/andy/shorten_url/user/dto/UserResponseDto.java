package io.andy.shorten_url.user.dto;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.entity.User;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final UserState state;
    private final UserRole role;
    private final Date createdAt;
    private final Date updatedAt;
    private final Date lastLoginAt;

    public UserResponseDto(User user) {
        id = user.getId();
        email = user.getEmail();
        state = user.getState();
        role = user.getRole();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        lastLoginAt = user.getLastLoginAt();
    }
}
