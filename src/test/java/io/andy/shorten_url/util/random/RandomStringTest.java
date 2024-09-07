package io.andy.shorten_url.util.random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RandomStringTest {
    private final RandomObject<StringBuilder> randomUtil = new RandomURL();

    @RepeatedTest(10)
    @DisplayName("get random string")
    public void generate() {
        int length = 22;
        StringBuilder randomString = randomUtil.generate(length);

        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        assertEquals(length, randomString.toString().length());
    }
}
