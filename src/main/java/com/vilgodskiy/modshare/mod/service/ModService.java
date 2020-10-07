package com.vilgodskiy.modshare.mod.service;

import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.mod.repository.ModRepository;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.UniqueStringFieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Service
@RequiredArgsConstructor
public class ModService {

    private final ModRepository modRepository;
    private final UniqueStringFieldValidator uniqueStringFieldValidator =
            new UniqueStringFieldValidator(Mod.ENTITY_NAME);

    /**
     * Create mod
     *
     * @param title - title
     * @param owner - who creator
     * @return - created mod
     */
    public Mod create(String title, User owner) {
        uniqueStringFieldValidator.validate(title, "Наименование",
                () -> modRepository.existsByTitleAndOwner(title, owner)).throwIfHasErrors();
        return new Mod(title, owner)
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
    public Mod update(UUID id, String title, User executor) {
        Mod mod = modRepository.getOrThrow(id);
        uniqueStringFieldValidator.validate(title, "Наименование",
                () -> modRepository.existsByTitleAndOwner(title, mod.getOwner())
                        && modRepository.getByTitleAndOwner(title, mod.getOwner()).getId() != id)
                .throwIfHasErrors();
        return mod.update(title, executor)
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
}
