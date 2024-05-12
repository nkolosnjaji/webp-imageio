package com.nkolosnjaji.webp.exceptions;

import java.util.Map;

import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_BAD_DIMENSION;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_BAD_WRITE;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_BITSTREAM_OUT_OF_MEMORY;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_FILE_TOO_BIG;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_INVALID_CONFIGURATION;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_NULL_PARAMETER;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_OUT_OF_MEMORY;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_PARTITION0_OVERFLOW;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_PARTITION_OVERFLOW;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_ERROR_USER_ABORT;
import static com.nkolosnjaji.webp.gen.LibWebP.VP8_ENC_OK;

public class WebPEncodingException extends WebPException {

    public static final Map<Integer, String> errors = Map.ofEntries(
            Map.entry(VP8_ENC_OK(), "Ok"),
            Map.entry(VP8_ENC_ERROR_OUT_OF_MEMORY(), "Memory error allocating objects"),
            Map.entry(VP8_ENC_ERROR_BITSTREAM_OUT_OF_MEMORY(), "Memory error while flushing bits"),
            Map.entry(VP8_ENC_ERROR_NULL_PARAMETER(), "A pointer parameter is NULL"),
            Map.entry(VP8_ENC_ERROR_INVALID_CONFIGURATION(), "Configuration is invalid"),
            Map.entry(VP8_ENC_ERROR_BAD_DIMENSION(), "Picture has invalid width/height"),
            Map.entry(VP8_ENC_ERROR_PARTITION0_OVERFLOW(), "Partition is bigger than 512k"),
            Map.entry(VP8_ENC_ERROR_PARTITION_OVERFLOW(), "Partition is bigger than 16M"),
            Map.entry(VP8_ENC_ERROR_BAD_WRITE(), "Error while flushing bytes"),
            Map.entry(VP8_ENC_ERROR_FILE_TOO_BIG(), "File is bigger than 4G"),
            Map.entry(VP8_ENC_ERROR_USER_ABORT(), "Abort request by user")
    );

    public WebPEncodingException(int code) {
        super("%s (error %d)".formatted(errors.get(code), code));
    }
}
