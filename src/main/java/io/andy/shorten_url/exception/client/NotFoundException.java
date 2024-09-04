package io.andy.shorten_url.exception.client;

import io.andy.shorten_url.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException  {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
