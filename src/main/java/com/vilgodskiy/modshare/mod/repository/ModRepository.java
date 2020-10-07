package com.vilgodskiy.modshare.mod.repository;

import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.util.GetOrThrowRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
public interface ModRepository extends JpaRepository<Mod, UUID>, JpaSpecificationExecutor<Mod>,
        GetOrThrowRepository<Mod, UUID> {

    String NOT_FOUND = "Мод не найден";

    @Override
    default String getNotFoundMessage() {
        return NOT_FOUND;
    }

    Boolean existsByTitleAndOwner(String title, User owner);

    Mod getByTitleAndOwner(String title, User owner);
}
