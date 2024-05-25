package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.imageio.gen.LibWebP;
import com.nkolosnjaji.webp.imageio.gen.WebPMemoryWriter;
import com.nkolosnjaji.webp.imageio.gen.WebPPicture;
import com.nkolosnjaji.webp.imageio.gen.WebPWriterFunction;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.Objects;

class InternalWriter {

    private final MemorySegment ms;

    public InternalWriter(Arena arena, InternalPicture picture) {
        Objects.requireNonNull(arena, "arena must not be null");
        Objects.requireNonNull(picture, "picture must not be null");

        this.ms = WebPMemoryWriter.allocate(arena);
        LibWebP.WebPMemoryWriterInit(this.ms);

        MemorySegment function = WebPWriterFunction.allocate(new MemoryWriterFunction(), arena);

        WebPPicture.writer(picture.getMemorySegment(), function);
        WebPPicture.custom_ptr(picture.getMemorySegment(), this.ms);
    }

    public ByteBuffer getEncodedBytes() {
        final long size = WebPMemoryWriter.size(this.ms);
        return WebPMemoryWriter.mem(this.ms).asSlice(0, size).asByteBuffer();
    }

    public void free() {
        LibWebP.WebPMemoryWriterClear(this.ms);
    }

    public static class MemoryWriterFunction implements WebPWriterFunction.Function {

        @Override
        public int apply(MemorySegment data, long dataSize, MemorySegment picture) {
            return LibWebP.WebPMemoryWrite(data, dataSize, picture);
        }
    }

}
