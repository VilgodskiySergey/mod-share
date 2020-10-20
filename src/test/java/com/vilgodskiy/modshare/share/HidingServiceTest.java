package com.vilgodskiy.modshare.share;

import com.vilgodskiy.modshare.share.service.HidingService;
import com.vilgodskiy.modshare.user.UserServiceTest;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vilgodskiy Sergey 20.10.2020
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class HidingServiceTest {

    @Autowired
    private HidingService hidingService;

    @Test
    void hideUserInfo() throws Exception {
        User user = UserServiceTest.createRandomUser();
        String root = "src/test/resources/test/HidingService/";
        String zipName = "test.zip";
        String toZip = root + zipName;
        String fileName = "test.txt";
        String toFile = "/test/" + fileName;
        Path rollbackFile = Path.of(root + "_" + zipName);
        Files.copy(Path.of(toZip), rollbackFile, StandardCopyOption.REPLACE_EXISTING);
        Assertions.assertDoesNotThrow(() -> hidingService.hideUserInfo(toZip, toFile, user));
        Assertions.assertDoesNotThrow(() -> FileUtil.extractFile(Path.of(toZip), toFile, Path.of(root + fileName)));
        try (BufferedReader br = new BufferedReader(new FileReader(root + fileName))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            Assertions.assertEquals("TEST", lines.get(0));
            Assertions.assertEquals(user.getId().toString(), lines.get(1));
        }
        Assertions.assertTrue(Path.of(root + fileName).toFile().delete());
        Files.copy(rollbackFile, Path.of(toZip), StandardCopyOption.REPLACE_EXISTING);
        Assertions.assertTrue(rollbackFile.toFile().delete());
    }
}
