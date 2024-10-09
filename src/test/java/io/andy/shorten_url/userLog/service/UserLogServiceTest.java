package io.andy.shorten_url.userLog.service;

import io.andy.shorten_url.user.constant.UserRole;
import io.andy.shorten_url.user.constant.UserState;
import io.andy.shorten_url.user.dto.UserResponseDto;
import io.andy.shorten_url.user.entity.User;
import io.andy.shorten_url.user.repository.UserRepository;
import io.andy.shorten_url.userLog.constant.UserLogMessage;
import io.andy.shorten_url.userLog.dto.AccessInfoDto;
import io.andy.shorten_url.userLog.dto.UpdateInfoDto;
import io.andy.shorten_url.userLog.entity.UserLog;
import io.andy.shorten_url.userLog.repository.UserLogRepository;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class UserLogServiceTest {
    @Autowired private UserLogRepository userLogRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserLogService userLogService;

    @BeforeEach
    public void flushBefore() {
        userLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void flushAfter() {
        userLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원정보 수정 UserLog 생성")
    void putUpdateInfoLog() {
        User user = setInitiateUser();
        String previousName = user.getUsername();
        String nextUsername = "new@gmail.com";

        userLogService.putUpdateInfoLog(new UpdateInfoDto(
                new UserResponseDto(user),
                UserLogMessage.UPDATE_USERNAME,
                previousName,
                nextUsername
        ));

        List<UserLog> result = userLogRepository.findByUserId(user.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(nextUsername, result.get(0).getPostValue());
    }

    @Test
    @DisplayName("회원가입 UserLog 생성")
    void putUserAccessLog() {
        User user = setInitiateUser();
        String ipAddress = "127.0.0.1";
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                UserLogMessage.SIGNUP,
                ipAddress,
                "firfox"
        ));

        List<UserLog> result = userLogRepository.findByUserId(user.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(ipAddress, result.get(0).getIpAddress());
    }

    @Test
    @DisplayName("userId 기반으로 UserLog 조회")
    void getUserLogsByUserId() {
        User user = setInitiateUser();
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                UserLogMessage.LOGIN,
                "127.0.0.1",
                "firefox"
        ));
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                UserLogMessage.LOGOUT,
                "127.0.0.1",
                "firefox"
        ));

        List<UserLog> result = userLogService.getUserLogsByUserId(user.getId());
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("LogMessage 기반 UserLog 생성")
    void getUserLogsByLogMessage() {
        User user = setInitiateUser();
        UserLogMessage message = UserLogMessage.LOGIN;
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                message,
                "127.0.0.1",
                "firefox"
        ));
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                message,
                "128.2.2.2",
                "chrome"
        ));

        List<UserLog> result = userLogService.getUserLogsByMessage(message);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(message, result.get(0).getMessage());
    }

    @Test
    @DisplayName("전체 UserLog 조회")
    public void findAllUserLogs() {
        User user = setInitiateUser();
        userLogService.putUserAccessLog(new AccessInfoDto(
                new UserResponseDto(user),
                UserLogMessage.LOGIN,
                "128.2.2.2",
                "chrome"
        ));
        userLogService.putUpdateInfoLog(new UpdateInfoDto(
                new UserResponseDto(user),
                UserLogMessage.UPDATE_USERNAME,
                user.getUsername(),
               "new@gmail.com"
        ));

        List<UserLog> result = userLogService.findAllUserLogs();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("최근 생성한 순으로 원하는 갯수만큼 UserLog 조회")
    public void findLatestUserLogs() {
        User user = setInitiateUser();
        for (int i = 0; i < 10; i++) {
            userLogService.putUserAccessLog(new AccessInfoDto(
                    new UserResponseDto(user),
                    UserLogMessage.LOGIN,
                    "127.0.0."+i,
                    "chrome+"+i
            ));
        }

        List<UserLog> result = userLogService.findLatestUserLogs(3);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    public User setInitiateUser() {
        String username = "hello@gmail.com";
        String password = "1234@5678";
        UserState state = UserState.NORMAL;
        UserRole role = UserRole.USER;
        return userRepository.save(new User(username, password, state, role));
    }
}