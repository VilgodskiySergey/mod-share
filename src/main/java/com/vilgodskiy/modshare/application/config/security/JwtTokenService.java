package com.vilgodskiy.modshare.application.config.security;

import com.auth0.jwt.JWT;
import com.vilgodskiy.modshare.application.config.security.domain.AccountJwtToken;
import com.vilgodskiy.modshare.application.config.security.domain.JwtTokenType;
import com.vilgodskiy.modshare.application.config.security.domain.Token;
import com.vilgodskiy.modshare.application.config.security.domain.TokenHolder;
import com.vilgodskiy.modshare.application.exception.AuthAccessDeniedException;
import com.vilgodskiy.modshare.user.api.UserAssembler;
import com.vilgodskiy.modshare.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    public static final String ID_CLAIM = "id";
    public static final String ROLE_CLAIM = "role";
    public static final String TOKEN_TYPE_CLAIM = "tokenType";
    public static final String SESSION_ID_CLAIM = "sessionId";

    @Value("${jwt.accessTokenExpiresIn}")
    private long accessTokenExpiresIn;

    private final TokenService tokenService;

    private final UserAssembler userAssembler;

    private final AccountService accountService;

    private final JwtTokenDecoder jwtTokenDecoder;

    @Transactional
    public Token issueToken(User user) {
        long accessTokenExpires = System.currentTimeMillis() + accessTokenExpiresIn;
        String sessionId = UUID.randomUUID().toString();
        String accessToken = createToken(JwtTokenType.ACCESS, user, accessTokenExpires, sessionId);

        tokenService.create(accessToken,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(accessTokenExpires), ZoneOffset.UTC),
                sessionId, user.getId());
        return new Token(accessToken, accessTokenExpires, userAssembler.assemble(user));
    }

    public AccountJwtToken decodeStringToken(String token) {
        return jwtTokenDecoder.decodeStringToken(token);
    }

    private User getAccount(UUID accountId) {
        User user = accountService.get(accountId).orElseThrow(() -> {
            tokenService.deleteByAccountId(accountId);
            throw new AuthAccessDeniedException("Пользователь не был найден");
        });
        if (user.isLocked()) {
            throw new AuthAccessDeniedException("Пользователь заблокирован");
        }
        return user;
    }

    private String createToken(JwtTokenType type, User user, long expires, String sessionId) {
        return JWT.create()
                .withSubject(user.getLogin())
                .withClaim(ID_CLAIM, user.getId().toString())
                .withClaim(TOKEN_TYPE_CLAIM, type.name())
                .withClaim(SESSION_ID_CLAIM, sessionId)
                .withArrayClaim(ROLE_CLAIM, new String[]{user.getRole().name()})
                .withExpiresAt(new Date(expires))
                .sign(jwtTokenDecoder.getAlgorithm());
    }

}
