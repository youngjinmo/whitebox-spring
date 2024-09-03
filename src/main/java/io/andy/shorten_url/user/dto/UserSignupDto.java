package io.andy.shorten_url.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSignUpDto {
    private final String email;
    private final String password;

    public UserSignUpDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
