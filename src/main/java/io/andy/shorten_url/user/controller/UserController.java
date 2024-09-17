package io.andy.shorten_url.user.controller;

import io.andy.shorten_url.session.SessionService;
import io.andy.shorten_url.user.dto.*;
import io.andy.shorten_url.user.service.UserService;

import io.andy.shorten_url.util.ClientMapper;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping("/user")
@RestController()
public class UserController {
    @Autowired private final UserService userService;
    @Autowired private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public UserResponseDto signUp(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String clientIp = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);

        return userService.createUserByUsername(new UserSignUpDto(username, password, clientIp, userAgent));
    }

    @PostMapping("/login")
    public UserResponseDto login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String clientIp = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);

        UserResponseDto user = userService.login(new UserLoginDto(username, password, clientIp, userAgent));
        sessionService.setAttribute(request, user.id());

        return user;
    }

    @DeleteMapping("/logout/{id}")
    public void logout(HttpServletRequest request, @PathVariable("id") Long id) {
        String clientIp = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);

        userService.logout(new UserLogOutDto(id, clientIp, userAgent));
        sessionService.invalidateSession(request, id);
    }

    @GetMapping("/all")
    public List<UserResponseDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto findUserById(@PathVariable("id") Long id) {
       return userService.findById(id);
    }

    @GetMapping("/find")
    public UserResponseDto findUserByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }

    @PatchMapping("/{id}/username")
    public UserResponseDto UpdateUsername(HttpServletRequest request, @PathVariable("id") Long id, @RequestParam String username) {
        validateSession(request);
        return userService.updateUsernameById(id, username);
    }

    @PatchMapping("/{id}/password")
    public UserResponseDto UpdatePassword(HttpServletRequest request, @PathVariable("id") Long id, @RequestParam String password) {
        validateSession(request);
        return userService.updatePasswordById(id, password);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(HttpServletRequest request, @PathVariable("id") Long id) {
        validateSession(request);

        String clientIp = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);
        log.info("user try to delete id={}, clientIp={}, user-agent={}", id, clientIp,userAgent);

        userService.deleteById(new UserDeleteDto(id, clientIp, userAgent));
    }

    private void validateSession(HttpServletRequest request) {
        if (Objects.isNull(sessionService.getAttribute(request))) {
            String clientIp = ClientMapper.parseClientIp(request);
            String userAgent = ClientMapper.parseUserAgent(request);

            log.debug("invalidate session, clientIp={}, user-agent={}", clientIp, userAgent);
            throw new IllegalStateException("INVALIDATE SESSION");
        }
    }
}
