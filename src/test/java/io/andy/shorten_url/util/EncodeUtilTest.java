package io.andy.shorten_url.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncodeUtilTest {

    @Test
    void encrypt() {
        String word = "hello";
        String encrypted = EncodeUtil.encrypt(word);

        assertNotNull(encrypted);
        assertNotEquals(word, encrypted);
    }
}