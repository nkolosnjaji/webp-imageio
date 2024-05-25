package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.imageio.exceptions.WebPWrongVersionException;
import com.nkolosnjaji.webp.imageio.gen.LibWebP;
import com.nkolosnjaji.webp.imageio.gen.WebPBitstreamFeatures;
import com.nkolosnjaji.webp.imageio.gen.WebPDecBuffer;
import com.nkolosnjaji.webp.imageio.gen.WebPDecoderConfig;
import com.nkolosnjaji.webp.imageio.gen.WebPDecoderOptions;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Objects;

import static com.nkolosnjaji.webp.imageio.gen.LibWebP.MODE_RGB;
import static com.nkolosnjaji.webp.imageio.gen.LibWebP.MODE_RGBA;

class InternalReader {

    private final MemorySegment ms;

    public InternalReader(Arena arena, WebP.Header header, WebPReaderParam imageReadParam) {
        Objects.requireNonNull(arena, "arena must not be null");
        Objects.requireNonNull(imageReadParam, "imageWriteParam must not be null");

        this.ms = WebPDecoderConfig.allocate(arena);
        if (LibWebP.WebPInitDecoderConfigInternal(this.ms, LibWebP.WEBP_DECODER_ABI_VERSION()) == 0) {
            throw new WebPWrongVersionException();
        }

        if (imageReadParam.getCrop() != null) {
            WebPDecoderOptions.crop_left(WebPDecoderConfig.options(this.ms), imageReadParam.getCrop().x());
            WebPDecoderOptions.crop_top(WebPDecoderConfig.options(this.ms), imageReadParam.getCrop().y());
            WebPDecoderOptions.crop_width(WebPDecoderConfig.options(this.ms), imageReadParam.getCrop().w());
            WebPDecoderOptions.crop_height(WebPDecoderConfig.options(this.ms), imageReadParam.getCrop().h());
            WebPDecoderOptions.use_cropping(WebPDecoderConfig.options(this.ms), 1);
        }

        if (imageReadParam.getResize() != null) {
            WebPDecoderOptions.scaled_width(WebPDecoderConfig.options(this.ms), imageReadParam.getResize().w());
            WebPDecoderOptions.scaled_height(WebPDecoderConfig.options(this.ms), imageReadParam.getResize().h());
            WebPDecoderOptions.use_scaling(WebPDecoderConfig.options(this.ms), 1);
        }

        if (Boolean.TRUE.equals(imageReadParam.getFlip())) {
            WebPDecoderOptions.flip(WebPDecoderConfig.options(this.ms), 1);
        }

        if(Boolean.TRUE.equals(imageReadParam.getMultiThreading())) {
            WebPDecoderOptions.use_threads(WebPDecoderConfig.options(this.ms), 1);
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

    public Dimension getImageDimensionAfterDecoding() {
        int width;
        int height;
        if (WebPDecoderOptions.use_scaling(WebPDecoderConfig.options(this.ms)) == 1)  {
            width = WebPDecoderOptions.scaled_width(WebPDecoderConfig.options(this.ms));
            height = WebPDecoderOptions.scaled_height(WebPDecoderConfig.options(this.ms));
        } else if (WebPDecoderOptions.use_cropping(WebPDecoderConfig.options(this.ms)) == 1) {
            width = WebPDecoderOptions.crop_width(WebPDecoderConfig.options(this.ms));
            height = WebPDecoderOptions.crop_height(WebPDecoderConfig.options(this.ms));
        }
        else {
            width = WebPBitstreamFeatures.width(WebPDecoderConfig.input(this.ms));
            height = WebPBitstreamFeatures.height(WebPDecoderConfig.input(this.ms));
        }
        return new Dimension(width, height);
    }
}
