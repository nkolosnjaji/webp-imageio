package com.nkolosnjaji.webp.imageio;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public final class WebPWriterSpi extends ImageWriterSpi {

    public WebPWriterSpi() {
        super("Nikola Kološnjaji",
                "1.0",
                new String[]{"WebP", "com/nkolosnjaji/webp"},
                new String[]{"com/nkolosnjaji/webp"},
                new String[]{"image/webp"},
                WebPWriter.class.getName(),
                new Class[]{ImageOutputStream.class, OutputStream.class},
                new String[]{WebPReaderSpi.class.getName()},
                false,
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null);
    }

    @Override
    public boolean canEncodeImage(ImageTypeSpecifier type) {
        return true;
    }

    @Override
    public ImageWriter createWriterInstance(Object extension) throws IOException {
        return new WebPWriter(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP JAVA encoder using Java Foreign Function by Nikola Kološnjaji";
    }

}
