package io.andy.shorten_url.user.entity;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter @Setter
@ToString
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserState state;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Date createdAt;
    private Date lastLoginAt;
    private Date updatedAt;
    private Date withdrawnAt;
    private Date deletedAt;

    public User() {}

    public User(String email,
                String password,
                UserState state,
                UserRole role,
                Date createdAt
    ) {
        this.email = email;
        this.password = password;
        this.state = state;
        this.role = role;
        this.createdAt = createdAt;
    }
}
