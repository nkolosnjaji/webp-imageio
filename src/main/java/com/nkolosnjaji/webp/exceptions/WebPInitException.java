package com.nkolosnjaji.webp.exceptions;

public class WebPInitException extends WebPException {

    public WebPInitException(String message) {
        super(message);
    }

    public WebPInitException(Throwable throwable) {
        super("WebP java init failed", throwable);
    }
}
