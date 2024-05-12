package com.nkolosnjaji.webp;

import com.nkolosnjaji.webp.imageio.WebPWriterParam;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.nkolosnjaji.webp.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageWriterTests {



    private static final String INPUT_DIR = "src/test/resources/images/input";

    private static final String OUTPUT_DIR = "src/test/resources/images/output";

    private static ImageWriter writer;

    @BeforeAll
    public static void init() {
        writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
    }

    @AfterAll
    public static void cleanup() throws IOException {
        try (Stream<Path> stream = Files.list(Path.of(OUTPUT_DIR))) {
            stream.forEach(fileLocation -> {
                try {
                    Files.delete(fileLocation);
                } catch (IOException e) {
                    // TODO log error
                }
            });
        }

    }

    @Test
    void imageWriterIsRegistered() {
        assertNotNull(writer);
        assertInstanceOf(WebPWriterParam.class, writer.getDefaultWriteParam());
        assertEquals("WebP JAVA encoder using Java Foreign Function by Nikola Kološnjaji",
                writer.getOriginatingProvider().getDescription(null));
    }

    public static String hexToVersion(int hexVersion) {
        int major = (hexVersion >> 16) & 0xFF;
        int minor = (hexVersion >> 8) & 0xFF;
        int revision = hexVersion & 0xFF;

        // Combine the components into a version string
        return "%d.%d.%d".formatted(major, minor, revision);
    }

    @Test
    void writeDefaultWithoutAlpha() {
        assertNotNull(writer);
        Path inputPath = WORKING_PATH.resolve(INPUT_DIR, "4.png");
        BufferedImage input = this.getInputFile(inputPath);

        Path outPath = WORKING_PATH.resolve(OUTPUT_DIR, "out_default_without_alpha.webp");

        assertDoesNotThrow(() -> writer.setOutput(new FileImageOutputStream(outPath.toFile())));
        assertDoesNotThrow(() -> writer.write(input));

        Path cwebp = WORKING_PATH.resolve(CWEBP_DIR, "4.webp");

        assertEqualFiles(cwebp , outPath);
    }

    @Test
    void writeDefaultWithAlpha() throws FileNotFoundException {
        assertNotNull(writer);
        Path inputPath = WORKING_PATH.resolve(INPUT_DIR, "alpha.png");
        BufferedImage inputImage = this.getInputFile(inputPath);

        Path outPath = WORKING_PATH.resolve(OUTPUT_DIR, "out_default_with_alpha.webp");


        assertDoesNotThrow(() -> writer.setOutput(new FileImageOutputStream(outPath.toFile())));
        assertDoesNotThrow(() -> writer.write(inputImage));

        Path cwebp = WORKING_PATH.resolve(CWEBP_DIR, "alpha.webp");

        assertEqualFiles(cwebp , outPath);

    }

    private BufferedImage getInputFile(Path path) {
        try (InputStream is = assertDoesNotThrow(() -> Files.newInputStream(path))) {
            return assertDoesNotThrow(() -> ImageIO.read(is));
        } catch (IOException e) {
           throw new RuntimeException(String.format("Unable to read input file from location: %s", path.toString()));
        }
    }
}
