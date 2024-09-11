package io.andy.shorten_url.user.entity;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@ToString
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserState state;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private LocalDateTime updatedAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime deletedAt;

    public User() {}

    public User(String username,
                String password,
                UserState state,
                UserRole role
    ) {
        this.username = username;
        this.password = password;
        this.state = state;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
}
