package io.andy.shorten_url.user.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class UserLoginDto {
    private final String email;
    private final String password;
    private final String ip;
    private final String agent;
    private final Date lastLoginAt;

    public UserLoginDto(String email, String password, String ip, String agent, Date lastLoginAt) {
        this.email = email;
        this.password = password;
        this.ip = ip;
        this.agent = agent;
        this.lastLoginAt = lastLoginAt;
    }
}
