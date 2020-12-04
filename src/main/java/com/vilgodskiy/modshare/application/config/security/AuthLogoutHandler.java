package com.vilgodskiy.modshare.application.config.security;

import com.google.common.net.HttpHeaders;
import com.vilgodskiy.modshare.application.config.security.domain.AccountJwtToken;
import com.vilgodskiy.modshare.application.config.security.domain.JwtTokenType;
import com.vilgodskiy.modshare.application.exception.AuthAccessDeniedException;
import com.vilgodskiy.modshare.application.exception.AuthExecutionConflictException;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vilgodskiy.modshare.application.config.security.WebSecurityConfig.ACCESS_TOKEN_PARAM;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
@RequiredArgsConstructor
public class AuthLogoutHandler implements LogoutHandler {

    private final JwtTokenService jwtTokenService;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token =
                request.getHeader(HttpHeaders.AUTHORIZATION) != null
                        ? request.getHeader(HttpHeaders.AUTHORIZATION)
                        : request.getParameter(ACCESS_TOKEN_PARAM);
        if (token == null) {
            throw new AuthAccessDeniedException("Токен отсутствует");
        }
        AccountJwtToken accessToken = jwtTokenService.decodeStringToken(token);
        if (accessToken.getTokenType() != JwtTokenType.ACCESS) {
            throw new AuthAccessDeniedException("Неверный тип токена");
        }
        if (!tokenService.deleteBySessionId(accessToken.getSessionId())) {
            throw new AuthExecutionConflictException("Сессия этого токена не зарегистрирована в системе");
        }
    }
}
