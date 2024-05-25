package com.nkolosnjaji.webp;

import com.nkolosnjaji.webp.imageio.Crop;
import com.nkolosnjaji.webp.imageio.Resize;
import com.nkolosnjaji.webp.imageio.WebPReaderParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static com.nkolosnjaji.webp.TestUtils.assertEqualColors;
import static com.nkolosnjaji.webp.TestUtils.getGeneratedPath;
import static org.junit.jupiter.api.Assertions.*;

class ImageReaderTests {

    private final ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

    @Test
    void imageReaderIsRegistered() {
        assertNotNull(reader);
        Assertions.assertInstanceOf(WebPReaderParam.class, reader.getDefaultReadParam());
    }


    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeDefault(String imageName) {
        Path input = getGeneratedPath(imageName);

        BufferedImage image = readImage(input);

        Color expectedColor = new Color(200,200,200);

        assertEqualColors(image, expectedColor);
        assertEquals(500, image.getHeight());
        assertEquals(500, image.getWidth());
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeCrop(String imageName) {
        Crop crop = new Crop(100, 100, 50, 75);
        WebPReaderParam param = new WebPReaderParam();
        param.setCrop(crop);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        Color expectedColor = new Color(200,200,200);

        assertEqualColors(image, expectedColor);
        assertEquals(50, image.getWidth());
        assertEquals(75, image.getHeight());

    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void decodeResize(String imageName) {
        Resize resize = new Resize(25, 50);
        WebPReaderParam param = new WebPReaderParam();
        param.setResize(resize);

        Path input = getGeneratedPath(imageName);
        BufferedImage image = readImage(input, param);

        Color expectedColor = new Color(200,200,200);

        assertEqualColors(image, expectedColor);
        assertEquals(25, image.getWidth());
        assertEquals(50, image.getHeight());

    }

    private BufferedImage readImage(Path input) {
        return readImage(input, null);
    }

    private BufferedImage readImage(Path input, WebPReaderParam param) {
        assertDoesNotThrow(() -> reader.setInput(new FileImageInputStream(input.toFile())));
        return assertDoesNotThrow(() -> reader.read(0, param));
    }

}
