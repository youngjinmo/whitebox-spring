package io.andy.shorten_url.user.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLoginDto {
    private final String username;
    private final String password;
    private final String ip;
    private final String agent;
    private final LocalDateTime lastLoginAt;

    public UserLoginDto(String username, String password, String ip, String agent) {
        this.username= username;
        this.password = password;
        this.ip = ip;
        this.agent = agent;
        this.lastLoginAt = LocalDateTime.now();
    }
}
