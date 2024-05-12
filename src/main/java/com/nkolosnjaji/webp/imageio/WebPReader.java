package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.exceptions.WebPException;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public final class WebPReader extends ImageReader {

    public static final String NOT_A_WEB_P_PICTURE = "Can not read image, must be a WebP format";
    private WebP.Header header;

    protected WebPReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    public ImageReadParam getDefaultReadParam() {
        return new WebPReaderParam();
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        if (imageIndex != 0) {
            throw new IOException("index doesn't exist");
        }
        checkHeader();
        if (param == null) {
            param = new WebPReaderParam();
        }
        return switch (this.getInput()) {
            case ImageInputStream iis -> WebP.decode(iis, this.header, param);
            case null, default -> throw new IllegalStateException(String.format("Unexpected value: %s", this.getInput().getClass()));
        };
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        checkHeader();
        return this.header.width();
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        checkHeader();
        return this.header.height();
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        return List.of(
                ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB),
                ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB)
        ).iterator();
    }

    @Override
    public int getNumImages(boolean allowSearch) throws IOException {
        return 1;
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        return null;
    }

    private void checkHeader() throws IOException {
        if (header == null && this.getInput() instanceof ImageInputStream iis) {
            iis.mark();
            try {
                header = WebP.getHeader(iis);
            }
            catch (WebPException e) {
                throw new IOException(NOT_A_WEB_P_PICTURE);
            } finally {
                iis.reset();
            }
        }
        else if (!(this.getInput() instanceof ImageInputStream)) {
            throw new IllegalStateException("Expecting type of ImageInputStream.class"); //TODO check if Input stream should be also supported
        }
        else {
            throw new IOException(NOT_A_WEB_P_PICTURE);
        }
    }

}
