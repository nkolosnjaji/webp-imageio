package com.nkolosnjaji.webp.imageio;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Locale;

public final class WebPReaderSpi extends ImageReaderSpi {

    public WebPReaderSpi() {
        super("Nikola Kološnjaji",
                "1.0",
                new String[]{"WebP"},
                new String[]{"webp"},
                new String[]{"image/webp"},
                WebPReader.class.getName(),
                new Class[]{ImageInputStream.class},
                new String[]{WebPWriterSpi.class.getName()},
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
    public boolean canDecodeInput(Object source) throws IOException {
        if (source instanceof ImageInputStream iis) {
            iis.mark();
            try {
                WebP.getHeader(iis);
                return true;
            }
            catch (Exception e) {
                return false;
            } finally {
                iis.reset();
            }

        }
        return false;
    }

    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new WebPReader(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP JAVA decoder using Java Foreign Function by Nikola Kološnjaji";
    }
}
