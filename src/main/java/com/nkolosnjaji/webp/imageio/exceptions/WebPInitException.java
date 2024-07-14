package com.nkolosnjaji.webp.imageio.exceptions;

public class WebPInitException extends WebPException {

    public WebPInitException(String message) {
        super(message);
    }

    public WebPInitException(Throwable throwable) {
        super("WebP java init failed", throwable);
    }

    public WebPInitException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
