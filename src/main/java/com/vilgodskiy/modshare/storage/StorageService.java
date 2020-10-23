package com.vilgodskiy.modshare.storage;

import com.vilgodskiy.modshare.mod.domain.Mod;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Vilgodskiy Sergey 23.10.2020
 */
public interface StorageService {

    /**
     * Download zip-file to system
     *
     * @param mod - mod entity
     * @return - path to downloaded file
     * @throws GeneralSecurityException - problems with auth in storage
     * @throws IOException              - problems with files
     */
    String downloadMod(Mod mod) throws GeneralSecurityException, IOException;
}
