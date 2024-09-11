package io.andy.shorten_url.user.service;

import io.andy.shorten_url.exception.client.UnauthorizedException;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.UserLogOutDto;
import io.andy.shorten_url.user.dto.UserLoginDto;
import io.andy.shorten_url.user.dto.UserResponseDto;
import io.andy.shorten_url.user.dto.UserSignUpDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUserByUsername(UserSignUpDto dto);
    UserResponseDto login(UserLoginDto dto) throws UnauthorizedException;
    void logout(UserLogOutDto dto);
    List<UserResponseDto> findAllUsers();
    UserResponseDto findById(Long id);
    UserResponseDto findByUsername(String username);
    UserResponseDto updateUsernameById(Long id, String username);
    UserResponseDto updatePasswordById(Long id, String password);
    UserResponseDto updateStateById(Long id, UserState state);
    void deleteById(Long id);
}
