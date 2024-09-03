package io.andy.shorten_url.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserLogOutDto {
    private final Long id;
    private final String clientIp;
    private final String userAgent;

    public UserLogOutDto(Long id, String clientIp, String userAgent) {
        this.id = id;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
