package io.andy.shorten_url.config;

import io.andy.shorten_url.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    private UserRepository userRepository;

    public WebConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
