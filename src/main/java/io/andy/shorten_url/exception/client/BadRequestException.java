package io.andy.shorten_url.exception.client;

import io.andy.shorten_url.exception.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "BAD REQUEST");
    }
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
