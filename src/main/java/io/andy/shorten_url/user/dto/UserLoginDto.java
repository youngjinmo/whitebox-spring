package io.andy.shorten_url.user.dto;

import java.time.LocalDateTime;

public record UserLoginDto(
        String username,
        String password,
        String ipAddress,
        String userAgent,
        LocalDateTime lastLoginAt
) {
    public UserLoginDto(
            String username,
            String password,
            String ipAddress,
            String userAgent
    ) {
        this(username, password, ipAddress, userAgent, LocalDateTime.now());
    }
}
