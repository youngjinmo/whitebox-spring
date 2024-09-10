package io.andy.shorten_url.user.service;

import io.andy.shorten_url.exception.client.BadRequestException;
import io.andy.shorten_url.exception.client.NotFoundException;
import io.andy.shorten_url.exception.client.UnauthorizedException;
import io.andy.shorten_url.exception.server.InternalServerException;
import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.*;
import io.andy.shorten_url.user.entity.User;
import io.andy.shorten_url.user.repository.UserRepository;
import io.andy.shorten_url.user_log.constant.UserLogMessage;
import io.andy.shorten_url.user_log.service.UserLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired private final PasswordEncoder passwordEncoder;
    @Autowired private final UserLogService userLogService;
    @Autowired private final UserRepository userRepository;
    private final Logger logger;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserLogService userLogService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userLogService = userLogService;
        this.userRepository = userRepository;
        this.logger = LoggerFactory.getLogger(UserServiceImpl.class);
    }

    @Override
    public UserResponseDto createUserByUsername(UserSignUpDto dto) {
        if (isDuplicateUsername(dto.getUsername())) {
            log.debug("this username is already exists = {}", dto.getUsername());
            throw new IllegalStateException("DUPLICATE USERNAME");
        }
        User newUser = new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                UserState.NEW,
                UserRole.USER
        );

        try {
            User user = userRepository.save(newUser);
            UserResponseDto userResponseDto = new UserResponseDto(user);

            log.debug("created user: {}", userResponseDto.toString());
            userLogService.saveUserInfoLog(userResponseDto, UserLogMessage.SIGNUP);

            return userResponseDto;
        } catch (Exception e) {
            log.error("failed to create user={}. error message={}", dto, e.getMessage());
            throw new InternalServerException("FAILED TO DELETE USER BY ID");
        }
    }

    @Override
    public UserResponseDto login(UserLoginDto dto) {
        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

                    user.setLastLoginAt(LocalDateTime.now());
                    user.setState(UserState.NORMAL);

                UserResponseDto userResponseDto = new UserResponseDto(user);
                logger.debug("user logined={}", userResponseDto.toString());
                userLogService.saveUserInfoLog(userResponseDto, UserLogMessage.LOGIN);

                return userResponseDto;
            }
            logger.debug("user failed to login by invalid password, id={}", user.getId());
            throw new UnauthorizedException("INVALID_PASSWORD");
        }
        logger.debug("failed to login by invalid username: {}", dto.getUsername());
        throw new UnauthorizedException("INVALID_USERNAME");
    }

    @Override
    public void logout(UserLogOutDto dto) {
        UserResponseDto user = this.findById(dto.getId()); // validate userId
        // TODO remove session
        userLogService.saveUserInfoLog(user, UserLogMessage.LOGOUT);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        /*
            1. userRepository.findAll()로 반환된 List<User>를 스트림으로 변환합니다.
            2. map(UserResponseDto::new)을 통해 각 User 객체를 UserResponseDto 생성자로 전달하여 변환합니다.
            3. collect(Collectors.toList())를 사용해 스트림을 다시 List<UserResponseDto>로 변환하여 반환합니다.
         */
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserResponseDto(user);
        }
        throw new NotFoundException("USER_NOT_FOUND");
    }

    @Override
    public UserResponseDto findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserResponseDto(user);
        }
        logger.debug("user failed to login by invalid username={}", username);
        throw new NotFoundException("USER NOT FOUND BY USERNAME");
    }

    @Override
    public UserResponseDto updateUsernameById(Long id, String username) {
        Optional<User> originUser = userRepository.findById(id);
        if (originUser.isPresent()) {
            User user = originUser.get();
            String before = user.getUsername();

            user.setUsername(username);
            user.setUpdatedAt(LocalDateTime.now());

            UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
            userLogService.saveUserInfoLog(
                    userResponseDto,
                    UserLogMessage.UPDATE_USERNAME,
                    before,
                    username
            );

            return userResponseDto;
        }
        logger.debug("failed to update username by invalid id={}, username={}", id, username);
        throw new BadRequestException("FAILED TO UPDATE USERNAME BY INVALID ID");
    }

    @Override
    public UserResponseDto updatePasswordById(Long id, String password) {
        Optional<User> originUser = userRepository.findById(id);
        if (originUser.isPresent()) {
            User user = originUser.get();

            user.setPassword(passwordEncoder.encode(password));
            user.setUpdatedAt(LocalDateTime.now());

            UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
            userLogService.saveUserInfoLog(userResponseDto, UserLogMessage.UPDATE_PASSWORD);

            return userResponseDto;
        }
        logger.debug("failed to update password by invalid id={}", id);
        throw new BadRequestException("FAILED TO UPDATE PASSWORD BY INVALID ID");
    }

    @Override
    public UserResponseDto updateStateById(Long id, UserState state) {
        Optional<User> originUser = userRepository.findById(id);
        if (originUser.isPresent()) {
            User user = originUser.get();
            UserState before = user.getState();

            user.setState(state); // execution update by jpa
            user.setUpdatedAt(LocalDateTime.now());

            UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
            userLogService.saveUserInfoLog(
                    userResponseDto,
                    UserLogMessage.UPDATE_STATE,
                    before.toString(),
                    state.toString()
            );

            return userResponseDto;
        }
        logger.debug("failed to update state by invalid id={}", id);
        throw new BadRequestException("FAILED TO UPDATE STATE BY INVALID ID");
    }

    @Override
    public void deleteById(UserDeleteDto dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent()) {
            try {
                userRepository.deleteById(dto.getId());
            } catch (Exception e) {
                logger.error("failed to delete user by id={}. error message={}", dto.getId(), e.getMessage());
                throw new InternalServerException("FAILED TO DELETE USER BY ID");
            }

            logger.info("user push to delete. id={}, ip={}, user-agent={}", dto.getId(), dto.getUserIp(), dto.getUserAgent());
            userLogService.saveUserActionLog(dto.getId(), UserLogMessage.DELETE_USER, dto.getUserIp(), dto.getUserAgent());
        } else {
            throw new BadRequestException("USER NOT FOUND BY ID");
        }
    }

    private boolean isDuplicateUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        return true;
    }
}
