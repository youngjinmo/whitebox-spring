package io.andy.shorten_url.user.controller;

import io.andy.shorten_url.session.SessionService;
import io.andy.shorten_url.user.dto.*;
import io.andy.shorten_url.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired private final UserService userService;
    @Autowired private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @ResponseBody
    @PostMapping("/create")
    public UserResponseDto SignUp(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        return userService.createUserByUsername(new UserSignUpDto(username, password, ip, userAgent));
    }

    @PostMapping("/login")
    public UserResponseDto Login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        UserResponseDto user = userService.login(new UserLoginDto(username, password, ip, userAgent));
        sessionService.setSessionById(request, user.getId());

        return user;
    }

    @DeleteMapping("/logout/{id}")
    public void logout(HttpServletRequest request, @PathVariable("id") Long id) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        userService.logout(new UserLogOutDto(id, ip, userAgent));
        sessionService.removeSessionById(request, id);
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

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        log.info("user try to delete id={}, ip={}, user-agent={}", id,ip,userAgent);

        userService.deleteById(new UserDeleteDto(id, ip, userAgent));
    }

    private void validateSession(HttpServletRequest request) {
        if (Objects.isNull(sessionService.getSession(request))) {
            String ip = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

            log.debug("invalidate session, ip={}, user-agent={}", ip,userAgent);
            throw new IllegalStateException("INVALIDATE SESSION");
        }
    }
}
