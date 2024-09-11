package io.andy.shorten_url.user.dto;

import lombok.Getter;

@Getter
public class UserLogOutDto {
    private final Long id;

    public UserLogOutDto(Long id) {
        this.id = id;
    }
}
