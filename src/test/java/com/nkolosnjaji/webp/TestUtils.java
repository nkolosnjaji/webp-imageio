package com.nkolosnjaji.webp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Adler32;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUtils {

    public static final Path WORKING_PATH = Path.of(System.getProperty("user.dir"));

    public static final String INPUT_DIR = "src/test/resources/images/input";

    public static final String OUTPUT_DIR = "target/test-classes/images/output";

    public static final String CWEBP_DIR = "src/test/resources/images/cwebp";

    private TestUtils() {
    }

    public static void assertEqualFiles(Path source, Path generated) {
        byte[] sourceBytes = assertDoesNotThrow(() -> Files.readAllBytes(source));
        byte[] generatedBytes = assertDoesNotThrow(() -> Files.readAllBytes(generated));

        Adler32 sourceChecksum = new Adler32();
        sourceChecksum.update(sourceBytes);
        final long sourceValue = sourceChecksum.getValue();

        Adler32 generatedChecksum = new Adler32();
        generatedChecksum.update(generatedBytes);
        final long generatedValue = generatedChecksum.getValue();

        assertEquals(sourceValue, generatedValue, "Checksums of source and generated pictures are not equal");
    }

    public static void assertEqualColors(BufferedImage image, Color expectedColor) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                assertEquals(expectedColor, new Color(image.getRGB(x, y)), "RGB colors not equal on x=%d,y=%d".formatted(x, y));
            }
        }
    }
}
