package io.andy.shorten_url.user.dto;

import lombok.Getter;

@Getter
public class UserDeleteDto {
    private final Long id;
    private final String clientIp;
    private final String userAgent;

    public UserDeleteDto(Long id, String clientIp, String userAgent) {
        this.id = id;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
