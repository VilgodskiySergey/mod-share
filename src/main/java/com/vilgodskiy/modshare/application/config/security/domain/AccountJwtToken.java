package com.vilgodskiy.modshare.application.config.security.domain;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.UUID;

import static com.vilgodskiy.modshare.application.config.security.JwtTokenService.*;

@Getter
public class AccountJwtToken extends UsernamePasswordAuthenticationToken {

    private String login;

    private UUID accountId;

    private JwtTokenType tokenType;

    private String sessionId;

    public AccountJwtToken(DecodedJWT jwt) {
        super(jwt.getSubject(), null,
                AuthorityUtils.createAuthorityList(jwt.getClaim(ROLE_CLAIM).asArray(String.class)));
        this.accountId = UUID.fromString(jwt.getClaim(ID_CLAIM).asString());
        this.login = jwt.getSubject();
        this.tokenType = JwtTokenType.valueOf(jwt.getClaim(TOKEN_TYPE_CLAIM).asString());
        this.sessionId = jwt.getClaim(SESSION_ID_CLAIM).asString();
    }
}
