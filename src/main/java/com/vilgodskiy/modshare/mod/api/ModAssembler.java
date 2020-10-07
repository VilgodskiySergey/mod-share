package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.user.api.UserAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Vilgodskiy Sergey 14.08.2020
 */
@Component
@RequiredArgsConstructor
public class ModAssembler {

    private final UserAssembler userAssembler;

    public ModResponse assemble(Mod mod) {
        return new ModResponse()
                .setId(mod.getId())
                .setTitle(mod.getTitle())
                .setRating(mod.getRating())
                .setOwner(userAssembler.assemble(mod.getOwner()));
    }
}