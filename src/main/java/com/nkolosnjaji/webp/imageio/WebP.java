package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.exceptions.WebPDecodingException;
import com.nkolosnjaji.webp.exceptions.WebPEncodingException;
import com.nkolosnjaji.webp.exceptions.WebPFormatException;
import com.nkolosnjaji.webp.gen.LibWebP;
import com.nkolosnjaji.webp.gen.WebPBitstreamFeatures;
import com.nkolosnjaji.webp.gen.WebPDecBuffer;
import com.nkolosnjaji.webp.gen.WebPPicture;
import com.nkolosnjaji.webp.gen.WebPRGBABuffer;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Path;

import static com.nkolosnjaji.webp.gen.LibWebP.WebPFreeDecBuffer;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

final class WebP {

    static {

        Path libSharpYuv = OsUtils.getPathLibSharpYuv();
        System.load(libSharpYuv.toFile().getAbsolutePath());

        Path libWebP = OsUtils.getPathLibWebP();
        System.load(libWebP.toFile().getAbsolutePath());
    }

    public static void encode(RenderedImage image, ImageWriteParam imageWriteParam, Object out) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image must not be null");
        }
        if (!(out instanceof OutputStream) && !(out instanceof ImageOutputStream)) {
            throw new IllegalStateException("out parameter must be %s or %s".formatted(
                    OutputStream.class.getSimpleName(),
                    ImageOutputStream.class.getSimpleName()
            ));
        }

        WebPWriterParam  param = switch (imageWriteParam) {
            case WebPWriterParam wwp -> wwp;
            case ImageWriteParam iwp ->  new WebPWriterParam(iwp.getCompressionQuality());
            case null -> new WebPWriterParam();
        };

        InternalWriter writer = null;

        try (Arena arena = Arena.ofConfined()) {
            InternalWriteConfig config = new InternalWriteConfig(arena, param);
            InternalPicture picture = new InternalPicture(arena, image);

            picture.resizeRescale(param);

            writer = new InternalWriter(arena, picture);

            try {
                final int result = LibWebP.WebPEncode(config.getMemorySegment(), picture.getMemorySegment());
                if (result != 1) {
                    throw new WebPEncodingException(WebPPicture.error_code(picture.getMemorySegment()));
                }
            }
            finally {
                picture.free();
            }

            switch (out) {
                case ImageOutputStream ios -> encodeImageOutputStream(writer.getEncodedBytes(), ios);
                case OutputStream os -> encodeOutputStream(writer.getEncodedBytes(), os);
                default -> throw new IllegalStateException(String.format("Unexpected value: %s", out.getClass()));
            }
            writer.free();
        }
        catch (Exception e) {
            if (writer != null) writer.free();
            throw e;
        }
    }

    private static void encodeOutputStream(ByteBuffer byteBuffer, OutputStream out) throws IOException {
        try (var channel = Channels.newChannel(out)) {
            channel.write(byteBuffer);
        }
    }

    private static void encodeImageOutputStream(ByteBuffer byteBuffer, ImageOutputStream ios) throws IOException {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        ios.write(bytes);
    }

    public static Header getHeader(ImageInputStream iis) throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            byte[] bytes;
            bytes = new byte[32];
            iis.readFully(bytes); // read 1st 32 bytes
            MemorySegment data = arena.allocate(bytes.length);

            for (int x = 0; x < bytes.length; x++) {
                data.setAtIndex(ValueLayout.JAVA_BYTE, x, bytes[x]);
            }

            MemorySegment features = WebPBitstreamFeatures.allocate(arena);
            int result = LibWebP.WebPGetFeaturesInternal(data, data.byteSize(), features, LibWebP.WEBP_DECODER_ABI_VERSION());

            if (result == LibWebP.VP8_STATUS_OK()) {
                final int width = WebPBitstreamFeatures.width(features);
                final int height = WebPBitstreamFeatures.height(features);
                final boolean hasAlpha = WebPBitstreamFeatures.has_alpha(features) == 1;
                final boolean hasAnimation = WebPBitstreamFeatures.has_animation(features) == 1;
                final int format = WebPBitstreamFeatures.format(features);
                return new Header(width, height, hasAlpha, hasAnimation, format);
            } else {
                throw new WebPFormatException();
            }
        }
    }

    public static BufferedImage decode(ImageInputStream iis, Header header, ImageReadParam param) throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            InternalReader reader = new InternalReader(arena, header, param);
            MemorySegment rawImage = InternalReader.createRaw(arena, iis);
            try {
                int result = LibWebP.WebPDecode(rawImage, rawImage.byteSize(), reader.getMemorySegment());

                if (result != LibWebP.VP8_STATUS_OK()) {
                    throw new WebPDecodingException(result);
                }

                MemorySegment buffer = reader.getBuffer();
                MemorySegment union = WebPDecBuffer.u(buffer);

                final long bufferSize = WebPRGBABuffer.size(union);
                final ByteBuffer byteBuffer = WebPRGBABuffer.rgba(union).asSlice(0, Math.toIntExact(bufferSize)).asByteBuffer();

                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);

                return getBufferedImage(header, bytes);
            } finally {
                WebPFreeDecBuffer(reader.getBuffer());
            }
        }
    }

    private static BufferedImage getBufferedImage(Header header, byte[] bytes) {
        int imageType = header.hasAlpha ? TYPE_INT_ARGB : TYPE_INT_RGB;
        BufferedImage bi = new BufferedImage(header.width(), header.height(), imageType);

        if (header.hasAlpha) {
            for (int y = 0; y < header.height; y++) {
                for (int x = 0; x < header.width; x++) {
                    int index = (y * header.width + x) * 4;
                    int r = bytes[index] & 0xFF;
                    int g = bytes[index + 1] & 0xFF;
                    int b = bytes[index + 2] & 0xFF;
                    int a = bytes[index + 3] & 0xFF;
                    Color c = new Color(r, g, b, a);
                    bi.setRGB(x, y, c.getRGB());
                }
            }
        } else {
            for (int y = 0; y < header.height; y++) {
                for (int x = 0; x < header.width; x++) {
                    int index = (y * header.width + x) * 3;
                    int r = bytes[index] & 0xFF;
                    int g = bytes[index + 1] & 0xFF;
                    int b = bytes[index + 2] & 0xFF;
                    Color c = new Color(r, g, b);
                    bi.setRGB(x, y, c.getRGB());
                }
            }
        }
        return bi;
    }

    public record Header(int width, int height, boolean hasAlpha, boolean hasAnimation, int format) {
    }

}
