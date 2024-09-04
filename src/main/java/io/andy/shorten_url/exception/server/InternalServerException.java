package io.andy.shorten_url.exception.server;

import io.andy.shorten_url.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException {
    public InternalServerException(String message) {
       super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
