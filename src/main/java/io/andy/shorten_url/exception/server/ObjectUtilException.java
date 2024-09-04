package io.andy.shorten_url.exception.server;

import io.andy.shorten_url.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ObjectUtilException extends CustomException  {
    public ObjectUtilException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
