package io.andy.shorten_url.user_log.entity;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user_log.constant.UserLogMessage;
import io.andy.shorten_url.user_log.dto.AccessInfoDto;
import io.andy.shorten_url.user_log.dto.UpdateInfoDto;
import io.andy.shorten_url.user_log.dto.UpdatePrivacyInfoDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
public class UserLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // to be update db schema
    private Long userId;
    private LocalDateTime createdAt;
    private String username;
    @Enumerated(EnumType.STRING)
    private UserState state;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserLogMessage message;
    private String preValue;
    private String postValue;
    private String ipAddress;
    private String userAgent;

    // JPA를 위한 디폴트 생성자
    public UserLog() {}

    // 사용자 접속 정보(SIGNUP/LOGIN/LOGOUT/DELETE)를 저장하는 UserLog 생성자
    public UserLog(AccessInfoDto userLogDto) {
        this.userId = userLogDto.getUserId();
        this.state = userLogDto.getState();
        this.role = userLogDto.getRole();
        this.message = userLogDto.getMessage();
        this.ipAddress = userLogDto.getIpAddress();
        this.userAgent = userLogDto.getUserAgent();
        this.createdAt = LocalDateTime.now();
    }

    // 사용자 변경 정보(UPDATE)를 저장하는 UserLog 생성자
    public UserLog(UpdateInfoDto userLogDto) {
        this.userId = userLogDto.getUserId();
        this.state = userLogDto.getState();
        this.role = userLogDto.getRole();
        this.message = userLogDto.getMessage();
        this.preValue = userLogDto.getPreValue();
        this.postValue = userLogDto.getPostValue();
        this.createdAt = LocalDateTime.now();
    }

    // 민감 정보 변경시 저장하는 UserLog 생성자
    public UserLog(UpdatePrivacyInfoDto userLogDto) {
        this.userId = userLogDto.getUserId();
        this.state = userLogDto.getState();
        this.role = userLogDto.getRole();
        this.message = userLogDto.getMessage();
        this.createdAt = LocalDateTime.now();
    }
}
