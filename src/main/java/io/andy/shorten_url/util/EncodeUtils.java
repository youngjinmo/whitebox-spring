package io.andy.shorten_url.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncodeUtils {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodeMessage(String message) {
        return passwordEncoder.encode(message);
    }

    public boolean matchMessage(String encodedMessage, String rawMessage) {
        return passwordEncoder.matches(encodedMessage, rawMessage);
    }
}
