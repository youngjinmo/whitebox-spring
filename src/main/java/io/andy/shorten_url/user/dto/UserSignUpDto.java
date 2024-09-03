package io.andy.shorten_url.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignUpDto {
    private final String username;
    private final String password;
    private final String ip;
    private final String userAgent;

    public UserSignUpDto(String username, String password, String ip, String userAgent) {
        this.username= username;
        this.password = password;
        this.ip = ip;
        this.userAgent = userAgent;
    }
}
