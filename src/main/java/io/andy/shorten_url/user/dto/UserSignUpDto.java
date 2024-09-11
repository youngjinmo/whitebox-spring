package io.andy.shorten_url.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignUpDto {
    private final String username;
    private final String password;

    public UserSignUpDto(String username, String password) {
        this.username= username;
        this.password = password;
    }
}
