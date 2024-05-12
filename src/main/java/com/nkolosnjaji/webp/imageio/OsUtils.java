package com.nkolosnjaji.webp.imageio;

import com.nkolosnjaji.webp.exceptions.WebPInitException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class OsUtils {

    private OsUtils() {}

    public static Path getPathLibSharpYuv() {
        return getLibPath("libsharpyuv");
    }

    public static Path getPathLibWebP() {
        return getLibPath("libwebp");
    }

    private static Path getLibPath(String libName) {
        SupportedOs os = SupportedOs.getCurrent();
        Path tempLocation = Path.of("/tmp/webp-java-imageio/lib/%s.%s".formatted(libName, os.suffix));
        try {
            Files.createDirectories(tempLocation.getParent());
        } catch (IOException e) {
            throw new WebPInitException(e);
        }

        try (InputStream lib = OsUtils.class.getResourceAsStream(getClassPathResource(libName, os))) {
            if (lib != null) {
                Files.copy(lib, tempLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            else throw new WebPInitException("Not found");
        } catch (IOException e) {
            throw new WebPInitException(e);
        }
        tempLocation.toFile().deleteOnExit();
        return tempLocation;
    }

    private static String getClassPathResource(String libName, SupportedOs os) {
        return "/lib/%s/%s/%s.%s".formatted(os.osName, os.arhc, libName, os.suffix);
    }

}
