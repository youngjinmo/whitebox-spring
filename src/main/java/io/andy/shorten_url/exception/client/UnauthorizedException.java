package io.andy.shorten_url.exception.client;

import io.andy.shorten_url.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
