package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.exceptions.WebPWrongVersionException;
import com.nkolosnjaji.webp.gen.LibWebP;
import com.nkolosnjaji.webp.gen.WebPDecBuffer;
import com.nkolosnjaji.webp.gen.WebPDecoderConfig;

import javax.imageio.ImageReadParam;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;

import static com.nkolosnjaji.webp.gen.LibWebP.MODE_RGB;
import static com.nkolosnjaji.webp.gen.LibWebP.MODE_RGBA;

class InternalReader {

    private final MemorySegment ms;

    public InternalReader(Arena arena, WebP.Header header, ImageReadParam imageReadParam) {
        Objects.requireNonNull(arena, "arena must not be null");
        Objects.requireNonNull(imageReadParam, "imageWriteParam must not be null");

        this.ms = WebPDecoderConfig.allocate(arena);
        if (LibWebP.WebPInitDecoderConfigInternal(this.ms, LibWebP.WEBP_DECODER_ABI_VERSION()) == 0) {
            throw new WebPWrongVersionException();
        }

        if (header.hasAlpha()) {
            WebPDecBuffer.colorspace(WebPDecoderConfig.output(this.ms), MODE_RGBA());
        } else {
            WebPDecBuffer.colorspace(WebPDecoderConfig.output(this.ms), MODE_RGB());
        }
    }

    public MemorySegment getMemorySegment() {
        return this.ms;
    }

    public MemorySegment getBuffer() {
        return WebPDecoderConfig.output(this.ms);
    }

    public static MemorySegment createRaw(Arena arena, ImageInputStream iis) throws IOException {

        byte[] bytes;
        bytes = new byte[Math.toIntExact(iis.length())];
        iis.readFully(bytes);

        MemorySegment data = arena.allocate(bytes.length);
        for (int x = 0; x < bytes.length; x++) {
            data.setAtIndex(ValueLayout.JAVA_BYTE, x, bytes[x]);
        }

        return data;
    }

}
