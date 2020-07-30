package com.vilgodskiy.modshare.application.config.security;

import com.vilgodskiy.modshare.application.config.security.domain.TokenHolder;
import com.vilgodskiy.modshare.application.config.security.domain.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public boolean deleteBySessionId(String sessionId) {
        return tokenRepository.deleteBySessionId(sessionId) > 0;
    }

    public void create(String accessToken, LocalDateTime accessTokenExpires, String sessionId, UUID accountId) {
        new TokenHolder(accessToken, accessTokenExpires, sessionId, accountId, false)
                .saveTo(tokenRepository);
    }

    public void delete(TokenHolder tokenHolder) {
        tokenRepository.deleteById(tokenHolder.getAccessToken());
    }

    public void revokeTokens(UUID accountId) {
        tokenRepository.revoke(accountId);
    }

    public void deleteByAccountId(UUID accountId) {
        tokenRepository.deleteByAccountId(accountId);
    }

    public boolean isRevoke(String accessToken) {
        Boolean revoke = tokenRepository.isRevoke(accessToken);
        return revoke == null || revoke;
    }
}
