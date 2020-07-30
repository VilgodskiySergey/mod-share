package com.vilgodskiy.modshare.application.config.security;

import com.vilgodskiy.modshare.application.config.security.domain.AccountJwtToken;
import com.vilgodskiy.modshare.application.config.security.domain.JwtTokenType;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.vilgodskiy.modshare.application.config.security.WebSecurityConfig.ACCESS_TOKEN_PARAM;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenService jwtTokenService;

    private final TokenService tokenService;

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtTokenService jwtTokenService,
                                  TokenService tokenService, UserRepository userRepository) {
        super(authManager);
        this.jwtTokenService = jwtTokenService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = getToken(req);

        if (token == null) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        AccountJwtToken accountJwtToken = jwtTokenService.decodeStringToken(token);
        if (userRepository.getOrThrow(accountJwtToken.getAccountId()).isLocked()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Вам отказано в доступе. Обратитесь к администратору");
        }
        if (tokenService.isRevoke(token)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Токен отозван");
        }
        if (accountJwtToken.getTokenType() != JwtTokenType.ACCESS) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Неверный формат токена");
        }
        return accountJwtToken;
    }

    private String getToken(HttpServletRequest req) {
        String token;
        if (req.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            String[] authorization = req.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
            if (authorization.length < 2 || !"Bearer".equals(authorization[0])) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Неверный тип авторизации");
            }
            token = authorization[1];
        } else {
            token = req.getParameter(ACCESS_TOKEN_PARAM);
        }
        return token;
    }
}
