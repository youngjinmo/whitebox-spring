package io.andy.shorten_url.user_log.dto;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.UserResponseDto;
import io.andy.shorten_url.user_log.constant.UserLogMessage;

import lombok.Getter;

@Getter
public class UpdateInfoDto {
    private final Long userId;
    private final UserState state;
    private final UserRole role;
    private final UserLogMessage message;
    private final String preValue;
    private final String postValue;

    public UpdateInfoDto(
            UserResponseDto userDto,
            UserLogMessage message,
            String preValue,
            String postValue
    ) {
        this.userId = userDto.getId();
        this.state = userDto.getState();
        this.role = userDto.getRole();
        this.message = message;
        this.preValue = preValue;
        this.postValue = postValue;
    }
}
