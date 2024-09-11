package io.andy.shorten_url.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncodeUtilsTest {

    @Autowired
    EncodeUtils encodeUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("문자열 encode 테스트")
    void encodeMessage() {
        String target = "12345678";
        String encoded = encodeUtils.encodeMessage(target);

        assertNotEquals(target, encoded);
    }

    @Test
    @DisplayName("encode 문자열 비교")
    void matchMessage() {
        String target = "12345678";
        String encoded = encodeUtils.encodeMessage(target);
        boolean result = encodeUtils.matchMessage(target, encoded);

        assertTrue(result);
    }
}