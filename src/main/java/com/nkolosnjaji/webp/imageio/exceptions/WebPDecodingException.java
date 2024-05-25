package com.nkolosnjaji.webp.imageio.exceptions;

import java.util.Map;

import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_BITSTREAM_ERROR;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_INVALID_PARAM;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_NOT_ENOUGH_DATA;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_OK;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_OUT_OF_MEMORY;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_SUSPENDED;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_UNSUPPORTED_FEATURE;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.VP8_STATUS_USER_ABORT;

public class WebPDecodingException extends WebPException {

    public static final Map<Integer, String> errors = Map.of(
            VP8_STATUS_OK(), "Ok",
            VP8_STATUS_OUT_OF_MEMORY(), "Out of memory",
            VP8_STATUS_INVALID_PARAM(), "Invalid param",
            VP8_STATUS_BITSTREAM_ERROR(), "Bitstream error",
            VP8_STATUS_UNSUPPORTED_FEATURE(), "Unsupported feature",
            VP8_STATUS_SUSPENDED(), "Process suspended",
            VP8_STATUS_USER_ABORT(), "User aborted",
            VP8_STATUS_NOT_ENOUGH_DATA(), "Not enough data"
    );

    private final int code;

    public WebPDecodingException(int code) {
        super("%s(%d)".formatted(errors.get(code), code));
        if (code == VP8_STATUS_OK()) {
            throw new IllegalArgumentException(String.format("Illegal success code %s", code));
        }
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
