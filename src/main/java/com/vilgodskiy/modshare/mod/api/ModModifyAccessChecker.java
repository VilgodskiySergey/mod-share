package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.application.exception.NotFoundObjectException;
import com.vilgodskiy.modshare.mod.repository.ModRepository;
import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ModModifyAccessChecker {

    private final ModRepository modRepository;

    public void checkAccessOrThrow(User executor, UUID id) {
        if (executor.getRole() == Role.ADMINISTRATOR) return;
        if (executor.getRole() == Role.MOD_DEVELOPER
                && modRepository.getOrThrow(id).getOwner().getId().equals(executor.getId())) {
            return;
        }
        throw new NotFoundObjectException(modRepository.getNotFoundMessage());
    }
}
