package com.vilgodskiy.modshare.share.service;

import com.vilgodskiy.modshare.application.exception.ExecutionConflictException;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.*;

@Service
@Slf4j
public class HidingService {

    public void hideUserInfo(String toZip, String toFileInZip, User user) throws Exception {
        String[] fileInZipPath = toFileInZip.split("/");
        String toTmpFile = "src/main/resources/" + fileInZipPath[fileInZipPath.length - 1];
        Path tmpFilePath = Path.of(toTmpFile);
        Path zipFilePath = Path.of(toZip);
        FileUtil.extractFile(zipFilePath, toFileInZip, tmpFilePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(toTmpFile, true))) {
            bw.newLine();
            bw.write(user.getId().toString());
        }
        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, null)) {
            Path fileInsideZipPath = fs.getPath(toFileInZip);
            Files.copy(tmpFilePath, fileInsideZipPath, StandardCopyOption.REPLACE_EXISTING);
        }
        if (!tmpFilePath.toFile().delete()) {
            log.error("Не удалось удалить файл: " + toTmpFile);
            throw new ExecutionConflictException("Не удалось удалить временный файл");
        }
    }

}
