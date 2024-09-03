package io.andy.shorten_url.user.controller;

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

import java.util.Date;
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
    public UserResponseDto SignUp(@RequestParam String email, @RequestParam String password) {
        logger.debug("email={},password={}", email, password);
        return userService.createUserByEmail(new UserSignUpDto(email, password));
    }

    @PostMapping("/login")
    public UserResponseDto Login(HttpServletRequest request, @RequestParam String email, @RequestParam String password) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        logger.debug("email={},password={},ip={}, userAgent={}", email, password, ip, userAgent);
        return userService.login(new UserLoginDto(email, password, ip, userAgent, new Date()));
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

    @GetMapping("/email")
    public UserResponseDto findUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @PatchMapping("/email/{id}")
    public UserResponseDto UpdateEmail(@PathVariable("id") Long id, @RequestParam String email) {
        return userService.updateEmailById(id, email);
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
