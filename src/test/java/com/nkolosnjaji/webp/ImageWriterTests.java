package com.nkolosnjaji.webp;

import com.nkolosnjaji.webp.imageio.Crop;
import com.nkolosnjaji.webp.imageio.Resize;
import com.nkolosnjaji.webp.imageio.WebPWriterParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.nkolosnjaji.webp.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTests {

    private static final ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

    @BeforeAll
    public static void init() {
        // create parent output folder
        final Path outputPath = getOutputPath();
        assertDoesNotThrow(() -> Files.createDirectories(outputPath));
    }

    @Test
    void imageWriterIsRegistered() {
        assertNotNull(writer);
        assertInstanceOf(WebPWriterParam.class, writer.getDefaultWriteParam());
        assertEquals("WebP JAVA encoder using Java Foreign Function by Nikola Kološnjaji",
                writer.getOriginatingProvider().getDescription(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeDefault(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);

        Path outPath = writeImage(bi, imageName);

        assertEqualFiles(outPath , imageName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeCompress(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_q50".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam(0.5f);
        Path outPath = writeImage(bi, outputName, param);


        assertEqualFiles(outPath , outputName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeMethod(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_m0".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam();
        param.setMethod(0);
        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writePreset(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_presetIcon".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam(WebPWriterParam.WebPWriterPreset.ICON);
        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeHint(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_hintPhoto".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam();
        param.setImageHint(WebPWriterParam.WebPWriterHint.PHOTO);
        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeCrop(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_crop100100100100".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam();
        param.setCrop(new Crop(100, 100, 100, 100));

        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeResize(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_resize100100".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam();
        param.setResize(new Resize(100, 100));
        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"rgba", "rgb"})
    void writeCropAndResize(String imageName) {
        Path input = getInputPath(imageName);
        BufferedImage bi = getInputFile(input);
        String outputName = "%s_resize100100_crop100100100100".formatted(imageName);

        WebPWriterParam param = new WebPWriterParam();
        param.setResize(new Resize(100, 100));
        param.setCrop(new Crop(100, 100, 100, 100));
        Path outPath = writeImage(bi, outputName, param);

        assertEqualFiles(outPath , outputName);
    }

    private BufferedImage getInputFile(Path imagePath) {
        try (InputStream is = assertDoesNotThrow(() -> Files.newInputStream(imagePath))) {
            return assertDoesNotThrow(() -> ImageIO.read(is));
        } catch (IOException e) {
           throw new RuntimeException(String.format("Unable to read input file from location: %s", imagePath.toString()));
        }
    }

    private Path writeImage(BufferedImage image, String name) {
        return writeImage(image, name, null);
    }

    private Path writeImage(BufferedImage image, String name, WebPWriterParam param) {
        Path outPath = WORKING_PATH.resolve(OUTPUT_DIR, "%s.webp".formatted(name));

        assertDoesNotThrow(() -> writer.setOutput(new FileImageOutputStream(outPath.toFile())));
        assertDoesNotThrow(() -> writer.write(null, new IIOImage(image, null, null), param));
//        assertDoesNotThrow(() -> writer.write(image));

        return outPath;
    }

    public static String hexToVersion(int hexVersion) {
        int major = (hexVersion >> 16) & 0xFF;
        int minor = (hexVersion >> 8) & 0xFF;
        int revision = hexVersion & 0xFF;

        // Combine the components into a version string
        return "%d.%d.%d".formatted(major, minor, revision);
    }
}
