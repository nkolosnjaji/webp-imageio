package com.nkolosnjaji.webp.imageio;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WebPWriter extends ImageWriter {

    public static final String COMPRESSION_LOSSY = "Lossy";
    public static final String COMPRESSION_LOSSLESS = "Lossless";

    protected WebPWriter(ImageWriterSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param) throws IOException {
        if (this.getOutput() != null) {
            if (param == null) {
                param = this.getDefaultWriteParam();
            }
            WebP.encode(image.getRenderedImage(), param, this.getOutput());
        }
        else {
            throw new RuntimeException("Unsupported type"); //TODO
        }

    }

    @Override
    public ImageWriteParam getDefaultWriteParam() {
        return new WebPWriterParam();
    }

    @Override
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata convertStreamMetadata(IIOMetadata inData, ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata convertImageMetadata(IIOMetadata inData, ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }
}
