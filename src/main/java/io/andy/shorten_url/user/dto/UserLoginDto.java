package io.andy.shorten_url.user.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLoginDto {
    private final String username;
    private final String password;
    private final String ip;
    private final String userAgent;
    private final LocalDateTime lastLoginAt;

    public UserLoginDto(String username, String password, String ip, String userAgent) {
        this.username= username;
        this.password = password;
        this.ip = ip;
        this.userAgent = userAgent;
        this.lastLoginAt = LocalDateTime.now();
    }
}
