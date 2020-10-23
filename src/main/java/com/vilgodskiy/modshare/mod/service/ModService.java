package com.vilgodskiy.modshare.mod.service;

import com.vilgodskiy.modshare.application.config.security.ActiveUserHolder;
import com.vilgodskiy.modshare.application.exception.ExecutionConflictException;
import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.mod.repository.ModRepository;
import com.vilgodskiy.modshare.share.service.HidingService;
import com.vilgodskiy.modshare.storage.StorageService;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.UniqueStringFieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModService {

    private final ModRepository modRepository;
    private final StorageService storageService;
    private final HidingService hidingService;

    private final UniqueStringFieldValidator uniqueStringFieldValidator =
            new UniqueStringFieldValidator(Mod.ENTITY_NAME);

    /**
     * Create mod
     *
     * @param title - title
     * @param owner - who creator
     * @return - created mod
     */
    public Mod create(String title, String googleDriveFileId, String zipName, String editingFilePath, User owner) {
        uniqueStringFieldValidator.validate(title, "Наименование",
                () -> modRepository.existsByTitleAndOwner(title, owner)).throwIfHasErrors();
        return new Mod(title, googleDriveFileId, zipName, editingFilePath, owner)
                .saveTo(modRepository);
    }

    /**
     * Update mod
     *
     * @param id       - mod ID
     * @param title    - new title
     * @param executor - who updated
     * @return - updated mod
     */
    public Mod update(UUID id, String title, String googleDriveFileId, String zipName, String editingFilePath,
                      User executor) {
        Mod mod = modRepository.getOrThrow(id);
        uniqueStringFieldValidator.validate(title, "Наименование",
                () -> modRepository.existsByTitleAndOwner(title, mod.getOwner())
                        && modRepository.getByTitleAndOwner(title, mod.getOwner()).getId() != id)
                .throwIfHasErrors();
        return mod.update(title, googleDriveFileId, zipName, editingFilePath, executor)
                .saveTo(modRepository);
    }

    /**
     * Delete mod
     *
     * @param id - mod ID
     */
    public void delete(UUID id) {
        modRepository.deleteById(id);
    }

    /**
     * Download mod
     *
     * @param id - mod ID
     * @return InputStream of mod zip-file
     */
    public InputStream downloadMod(UUID id) {
        Mod mod = modRepository.getOrThrow(id);
        try {
            String zipPath = storageService.downloadMod(mod);
            hidingService.hideUserInfo(zipPath, mod.getEditingFilePath(), ActiveUserHolder.getActiveUser());
            return Files.newInputStream(Path.of(zipPath), StandardOpenOption.DELETE_ON_CLOSE);
        } catch (Exception e) {
            log.error("Can't download file to Mod share system", e);
            throw new ExecutionConflictException("Не удалось скачать мод. Обратитесь к администратору");
        }
    }
}
