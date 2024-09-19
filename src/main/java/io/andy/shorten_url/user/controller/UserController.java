package io.andy.shorten_url.user.controller;

import io.andy.shorten_url.exception.client.UnauthorizedException;
import io.andy.shorten_url.exception.server.InternalServerException;
import io.andy.shorten_url.session.SessionService;
import io.andy.shorten_url.user.dto.*;
import io.andy.shorten_url.user.service.UserService;
import io.andy.shorten_url.util.ClientMapper;
import io.andy.shorten_url.util.mail.MailService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired private final UserService userService;
    @Autowired private final SessionService sessionService;
    @Autowired private final MailService mailService;

    @PostMapping("/create")
    public UserResponseDto signUp(@RequestBody UserSignUpDto userDto) {
        return userService.createUserByUsername(userDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(HttpServletRequest request, @RequestBody UserLoginDto userDto) {
        String userAgent = ClientMapper.parseUserAgent(request);
        UserResponseDto user = userService.login(userDto, userAgent);
        sessionService.setAuthSession(request, user.id());

        return user;
    }

    @PostMapping("/send-email-auth")
    public ResponseEntity<String> sendEmailAuth(HttpServletRequest request, @RequestParam String mailRecipient) {
        try {
            String secretCode = mailService.sendMail(mailRecipient);
            sessionService.setMailAuthSession(request, secretCode);
            return new ResponseEntity<>(secretCode, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed to sent mail = {}, error message={}", mailRecipient, e.getMessage());
            throw new InternalServerException("FAILED TO SENT MAIL");
        }
    }

    @GetMapping("/verify-email-auth")
    public boolean verifyEmailAuth(HttpServletRequest request, @RequestParam String secretCode) {
        Object authSession = sessionService.getAuthSession(request);
        if (Objects.isNull(authSession)) return false;
        return authSession == secretCode;
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

    @GetMapping("/check-username-available")
    public boolean findUserByUsername(@RequestParam String username) {
        return userService.isDuplicateUsername(username);
    }

    @PatchMapping("/{id}/username")
    public UserResponseDto UpdateUsername(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody String username) {
        validateSession(request, id);
        return userService.updateUsernameById(id, username);
    }

    @PatchMapping("/{id}/password")
    public UserResponseDto UpdatePassword(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody String password) {
        validateSession(request, id);
        return userService.updatePasswordById(id, password);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(HttpServletRequest request, @PathVariable("id") Long id) {
        validateSession(request, id);

        String clientIp = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);
        log.info("user try to delete id={}, clientIp={}, user-agent={}", id, clientIp,userAgent);

        userService.deleteById(new UserDeleteDto(id, clientIp, userAgent));
    }

    private void validateSession(HttpServletRequest request, Long id) {
        Object authSession = sessionService.getAuthSession(request);
        if (Objects.isNull(authSession)) throw new UnauthorizedException();
        if (authSession != id) throw new UnauthorizedException();
    }
}
