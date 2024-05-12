package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.exceptions.WebPException;
import com.nkolosnjaji.webp.exceptions.WebPWrongVersionException;
import com.nkolosnjaji.webp.gen.LibWebP;
import com.nkolosnjaji.webp.gen.WebPPicture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.nkolosnjaji.webp.gen.LibWebP.WebPPictureRescale;
import static com.nkolosnjaji.webp.gen.LibWebP.WebPPictureView;

class InternalPicture {
    private final MemorySegment ms;

    public InternalPicture(Arena arena, RenderedImage image) {
        Objects.requireNonNull(arena, "arena must not be null");
        this.ms = WebPPicture.allocate(arena);
        if (LibWebP.WebPPictureInitInternal(ms, LibWebP.WEBP_ENCODER_ABI_VERSION()) == 0) {
            throw new WebPWrongVersionException();
        }
        MemorySegment data = importData(arena, image);
        WebPPicture.width(this.ms, image.getWidth());
        WebPPicture.height(this.ms, image.getHeight());

        int importResult;
        if (image.getColorModel().hasAlpha()) {
            importResult = LibWebP.WebPPictureImportBGRA(this.ms, data, image.getWidth() * 4);
        } else {
            importResult = LibWebP.WebPPictureImportBGR(this.ms, data, image.getWidth() * 3);
        }

        if (importResult != 1) {
            this.free();
            throw new WebPException("Error allocating picture"); // TODO
        }

    }

    public void free() {
        LibWebP.WebPPictureFree(this.ms);
    }

    public MemorySegment getMemorySegment() {
        return ms;
    }

    private static MemorySegment importData(Arena arena, RenderedImage image) {

        if (image.getColorModel().getTransferType() != DataBuffer.TYPE_BYTE) {
            throw new RuntimeException("invalid DataBuffer type");
        }

        DataBufferByte dataBuffer = (DataBufferByte) image.getData().getDataBuffer();

        final byte[] data = dataBuffer.getData();

        if (image.getColorModel().hasAlpha()) {
            IntStream.range(0, data.length / 4).forEach(i -> {
                var t1 = data[i * 4];
                var t2 = data[i * 4 + 1];
                var t3 = data[i * 4 + 2];
                var t4 = data[i * 4 + 3];

                data[i * 4] = t2;
                data[i * 4 + 1] = t3;
                data[i * 4 + 2] = t4;
                data[i * 4 + 3] = t1;
            });
        }
//        else {
//            IntStream.range(0, data.length / 3).forEach(i -> {
//                var t1 = data[i * 3];
//                var t2 = data[i * 3 + 1];
//                var t3 = data[i * 3 + 2];
//
//                data[i * 3] = t1;
//                data[i * 3 + 1] = t2;
//                data[i * 3 + 2] = t3;
//            });
//        }

        return arena.allocateFrom(ValueLayout.JAVA_BYTE, data);
    }

    public void resizeRescale(WebPWriterParam param) {
        if (param.getCrop() != null) {
            final WebPWriterParam.Crop crop = param.getCrop();
            if (WebPPictureView(
                    this.ms,
                    crop.x(),
                    crop.y(),
                    crop.w(),
                    crop.h(),
                    this.ms) != 1) {
                throw new WebPException("Error during cropping");
            }
        }

        if (param.getResize() != null) {
            final int w = param.getResize().w();
            final int h = param.getResize().h();
            if (WebPPictureRescale(this.ms, w, h) != 1) {
                throw new WebPException("Error during rescaling");
            }
        }
    }


}
