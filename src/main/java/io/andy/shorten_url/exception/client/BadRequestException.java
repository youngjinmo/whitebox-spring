package io.andy.shorten_url.exception.client;

import io.andy.shorten_url.exception.CustomException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
