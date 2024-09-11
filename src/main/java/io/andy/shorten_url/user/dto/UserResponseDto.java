package io.andy.shorten_url.user.dto;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.entity.User;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class UserResponseDto {
    private final Long id;
    private final String username;
    private final UserState state;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime lastLoginAt;
    private final LocalDateTime withdrawnAt;
    private final LocalDateTime deletedAt;

    public UserResponseDto(User user) {
        id = user.getId();
        username = user.getUsername();
        state = user.getState();
        role = user.getRole();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        lastLoginAt = user.getLastLoginAt();
        withdrawnAt = user.getWithdrawnAt();
        deletedAt = user.getDeletedAt();
    }
}
