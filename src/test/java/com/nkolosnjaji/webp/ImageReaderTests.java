package com.nkolosnjaji.webp;

import com.nkolosnjaji.webp.imageio.WebPReaderParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Iterator;

import static com.nkolosnjaji.webp.TestUtils.CWEBP_DIR;
import static com.nkolosnjaji.webp.TestUtils.assertEqualColors;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
class ImageReaderTests {

    private final Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType("image/webp");

    @Test
    @Order(1)
    void imageReaderIsRegistered() {
        assertTrue(readers.hasNext());
        Assertions.assertInstanceOf(WebPReaderParam.class, readers.next().getDefaultReadParam());
    }

    @Test
    void canDecodeWithoutAlpha() {
        Path input = TestUtils.WORKING_PATH.resolve(CWEBP_DIR, "out.webp");

        ImageReader reader = readers.next();
        assertDoesNotThrow(() -> reader.setInput(new FileImageInputStream(input.toFile())));
        BufferedImage image = assertDoesNotThrow(() -> reader.read(0));

        Color expectedColor = new Color(127,127,127);

        assertEqualColors(image, expectedColor);
    }

    //TODO fix
//    @Test
    void canDecodeWithAlpha() {
        Path input = TestUtils.WORKING_PATH.resolve(CWEBP_DIR, "yes_alpha.webp");

        ImageReader reader = readers.next();
        assertDoesNotThrow(() -> reader.setInput(new FileImageInputStream(input.toFile())));

        BufferedImage image = assertDoesNotThrow(() -> reader.read(0));

        Color expectedColor = new Color(144, 144, 144, 255);

        assertEqualColors(image, expectedColor);
    }

}
