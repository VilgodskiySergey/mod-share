package com.vilgodskiy.modshare.application.config.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public interface TokenRepository extends CrudRepository<TokenHolder, String> {

    @Modifying
    @Transactional
    int deleteBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("UPDATE TokenHolder t SET t.revoked = true WHERE t.accountId = :accountId")
    void revoke(UUID accountId);

    @Modifying
    @Transactional
    void deleteByAccountId(UUID accountId);

    @Query("SELECT t.revoked FROM TokenHolder t WHERE t.accessToken = :accessToken")
    Boolean isRevoke(String accessToken);
}
