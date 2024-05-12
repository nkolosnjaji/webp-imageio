package com.nkolosnjaji.webp.exceptions;

public class WebPException extends RuntimeException {

    public WebPException(String message) {
        super(message);
    }

    public WebPException(String message, Throwable cause) {
        super(message, cause);
    }
}
