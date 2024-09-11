package io.andy.shorten_url.user.controller;

import io.andy.shorten_url.exception.client.BadRequestException;
import io.andy.shorten_url.user.dto.UserLogOutDto;
import io.andy.shorten_url.user.dto.UserLoginDto;
import io.andy.shorten_url.user.dto.UserResponseDto;
import io.andy.shorten_url.user.dto.UserSignUpDto;
import io.andy.shorten_url.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/create")
    public UserResponseDto SignUp(@RequestParam String username, @RequestParam String password) {
        logger.debug("username={},password={}", username, password);
        return userService.createUserByUsername(new UserSignUpDto(username, password));
    }

    @PostMapping("/login")
    public UserResponseDto Login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        logger.debug("username={},password={},ip={}, userAgent={}", username, password, ip, userAgent);
        return userService.login(new UserLoginDto(username, password, ip, userAgent));
    }

    @DeleteMapping("/logout/{id}")
    public void logout(@PathVariable("id") Long userId) {
        userService.logout(new UserLogOutDto(userId));
    }

    @GetMapping("/all")
    public List<UserResponseDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto findUserById(@PathVariable("id") Long id) {
       return userService.findById(id);
    }

    @GetMapping("/")
    public UserResponseDto findUserByUsername(@RequestParam String username) {
        return userService.findByUsername(username);
    }

    @PatchMapping("/{id}")
    public UserResponseDto UpdateUsername(@PathVariable("id") Long id, @RequestParam String username) {
        return userService.updateUsernameById(id, username);
    }

    @PatchMapping("/password/{id}")
    public UserResponseDto UpdatePassword(@PathVariable("id") Long id, @RequestParam String password) {
        return userService.updatePasswordById(id, password);
    }

    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request, @PathVariable("id") Long id) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        logger.info("user try to delete id={}, ip={}, user-agent={}", id,ip,userAgent);
        userService.deleteById(id);
    }
}
