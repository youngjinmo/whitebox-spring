package io.andy.shorten_url.user.service;

import io.andy.shorten_url.exception.client.BadRequestException;
import io.andy.shorten_url.exception.client.NotFoundException;
import io.andy.shorten_url.exception.client.UnauthorizedException;
import io.andy.shorten_url.exception.server.InternalServerException;
import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.UserLogOutDto;
import io.andy.shorten_url.user.dto.UserLoginDto;
import io.andy.shorten_url.user.dto.UserResponseDto;
import io.andy.shorten_url.user.dto.UserSignUpDto;
import io.andy.shorten_url.user.entity.User;
import io.andy.shorten_url.user.repository.UserRepository;
import io.andy.shorten_url.user_log.UserLogMessage;
import io.andy.shorten_url.user_log.service.UserLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public UserResponseDto createUserByEmail(UserSignUpDto dto) {
        try {
            if (isDuplicateEmail(dto.getEmail())) {
                logger.debug("this email is already exists = {}", dto.getEmail());
                throw new IllegalAccessException("DUPLICATE EMAIL");
            }
            User user = userRepository.save(new User(
                    dto.getEmail(),
                    passwordEncoder.encode(dto.getPassword()),
                    UserState.NEW,
                    UserRole.USER,
                    new Date()
            ));
            UserResponseDto userResponseDto = new UserResponseDto(user);
            logger.debug("created user: {}", userResponseDto.toString());
            saveUserLogByInfo(userResponseDto, UserLogMessage.SIGNUP);

            return userResponseDto;
        } catch (Exception e) {
            logger.error("failed to create new user: email={}. error message={}", dto.getEmail(), e.getMessage());
            throw new InternalServerException("FAILED TO SIGNUP");
        }
    }

    @Override
    public UserResponseDto login(UserLoginDto dto) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                    user.setLastLoginAt(new Date());
                    user.setState(UserState.NORMAL);

                    logger.debug("user logined={}", user);
                    UserResponseDto userResponseDto = new UserResponseDto(user);
                    saveUserLogByInfo(userResponseDto, UserLogMessage.LOGIN);

                    return userResponseDto;
                }
                logger.debug("user failed to login by invalid password, id={}", user.getId());
                throw new UnauthorizedException("INVALID_PASSWORD");
            }
            logger.debug("failed to login by invalid email: {}", dto.getEmail());
            throw new UnauthorizedException("INVALID_EMAIL");
        } catch (Exception e) {
            if (e instanceof UnauthorizedException) {
                throw e;
            }
            logger.error("failed to login. error message={}", e.getMessage());
            throw new InternalServerException("FAILED TO LOGIN");
        }
    }

    @Override
    public void logout(UserLogOutDto dto) {
       try {
           // TODO check session and remove session
           UserResponseDto user = this.findById(dto.getId()); // validate userId
           saveUserLogByInfo(user, UserLogMessage.LOGOUT);
       } catch (Exception e) {
           logger.error("failed to logout user, id={}. error message={}", dto.getId(), e.getMessage());
       }
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
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return new UserResponseDto(user);
            }
            throw new NotFoundException("USER_NOT_FOUND");
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            }
            logger.error("failed to find user by id={}. error message={}", id, e.getMessage());
            throw new InternalServerException("FAILED TO FIND USER BY ID");
        }
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return new UserResponseDto(user);
            }
            throw new NotFoundException("USER NOT FOUND BY EMAIL");
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            }
            logger.error("failed to find user by email={}. error message={}", email, e.getMessage());
            throw new InternalServerException("FAILED TO FIND USER BY EMAIL");
        }
    }

    @Override
    public UserResponseDto updateEmailById(Long id, String email) {
        try {
            Optional<User> originUser = userRepository.findById(id);
            if (originUser.isPresent()) {
                User user = originUser.get();
                user.setEmail(email); // execution update by jpa
                user.setUpdatedAt(new Date());

                UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
                saveUserLogByInfo(userResponseDto, UserLogMessage.UPDATE_EMAIL);

                return userResponseDto;
            }
            logger.debug("failed to update email by invalid id={}, email={}", id, email);
            throw new BadRequestException("FAILED TO UPDATE EMAIL BY INVALID ID");
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw new BadRequestException("FAILED TO UPDATE EMAIL BY INVALID ID");
            }
            logger.error("failed to update email user: id={}, new email={}. error message={}", id, email, e.getMessage());
            throw new InternalServerException("FAILED TO UPDATE EMAIL");
        }
    }

    @Override
    public UserResponseDto updatePasswordById(Long id, String password) {
        try {
            Optional<User> originUser = userRepository.findById(id);
            if (originUser.isPresent()) {
                User user = originUser.get();
                user.setPassword(passwordEncoder.encode(password)); // execution update by jp
                user.setUpdatedAt(new Date());

                UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
                saveUserLogByInfo(userResponseDto, UserLogMessage.UPDATE_PASSWORD);

                return userResponseDto;
            }
            logger.debug("failed to update password by invalid id={}", id);
            throw new BadRequestException("FAILED TO UPDATE PASSWORD BY INVALID ID");
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw e;
            }
            logger.error("failed to update password, id={}. error message={}", id, e.getMessage());
            throw new InternalServerException("FAILED TO UPDATE PASSWORD BY ID");
        }
    }

    @Override
    public UserResponseDto updateStateById(Long id, UserState state) {
        try {
            Optional<User> originUser = userRepository.findById(id);
            if (originUser.isPresent()) {
                User user = originUser.get();
                user.setState(state); // execution update by jp
                user.setUpdatedAt(new Date());

                UserResponseDto userResponseDto = new UserResponseDto(userRepository.findById(id).get());
                saveUserLogByInfo(userResponseDto, UserLogMessage.UPDATE_STATE);

                return userResponseDto;
            }
            logger.debug("failed to update state by invalid id={}", id);
            throw new BadRequestException("FAILED TO UPDATE STATE BY INVALID ID");
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw e;
            }
            logger.error("failed to update state, id={}. error message={}", id, e.getMessage());
            throw new InternalServerException("FAILED TO UPDATE STATE BY ID");
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.debug("user try to delete id={}", id);
            userRepository.deleteById(id);
            saveUserLogByInfo(new UserResponseDto(user.get()), UserLogMessage.DELETE_USER);
        } else {
            throw new BadRequestException("USER NOT FOUND BY ID");
        }
    }

    private void saveUserLogByInfo(UserResponseDto user, UserLogMessage message) {
        try {
            // userLogService.saveUserInfoLog(user, message); TODO jpa 학습 후 재도전..
            logger.info(message.toString()+" : id="+user.getId());
        } catch (Exception e) {
            logger.warn("failed to save user log: {}", e.getMessage());
        }
    }

    private boolean isDuplicateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        }
        return true;
    }
}
