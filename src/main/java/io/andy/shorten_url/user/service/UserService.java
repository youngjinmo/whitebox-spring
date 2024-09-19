package io.andy.shorten_url.user.service;

import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.*;

import java.util.List;

public interface UserService {
    UserResponseDto createUserByUsername(UserSignUpDto userDto);
    UserResponseDto login(UserLoginDto userDto, String userAgent);
    void logout(UserLogOutDto userDto);
    List<UserResponseDto> findAllUsers();
    UserResponseDto findById(Long id);
    UserResponseDto findByUsername(String username);
    UserResponseDto updateUsernameById(Long id, String username);
    UserResponseDto updatePasswordById(Long id, String password);
    UserResponseDto updateStateById(Long id, UserState state);
    void deleteById(UserDeleteDto dto);
    boolean isDuplicateUsername(String username);
}
