package com.vilgodskiy.modshare.util;

import java.io.IOException;
import java.nio.file.*;

public class FileUtil {

    public static void extractFile(Path zipFile, String fileName, Path outputFile) throws IOException {
        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null)) {
            Path fileToExtract = fileSystem.getPath(fileName);
            Files.copy(fileToExtract, outputFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
