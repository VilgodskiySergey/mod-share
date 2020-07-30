package com.vilgodskiy.modshare.application.config.security;

import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vilgodskiy.modshare.application.config.security.domain.CurrentUser;
import com.vilgodskiy.modshare.application.config.security.domain.LoginForm;
import com.vilgodskiy.modshare.application.config.security.domain.Token;
import com.vilgodskiy.modshare.application.exception.AuthExecutionConflictException;
import com.vilgodskiy.modshare.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper;
    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        LoginForm creds;
        try {
            creds = mapper.readValue(req.getInputStream(), LoginForm.class);
        } catch (IOException e) {
            throw new AuthExecutionConflictException("Ошибка чтения запроса на этапе аутентификации", e);
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getLogin(),
                        creds.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        User user = ((CurrentUser) auth.getPrincipal()).getUser();
        try {
            Token token = jwtTokenService.issueToken(user);
            res.addHeader("Content-Type", "application/json;charset=UTF-8");
            String httpBody = mapper.writeValueAsString(token);
            res.getWriter().append(httpBody);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Не удалось получить токен");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();
        ValidationResult message;
        if (failed.getClass().equals(BadCredentialsException.class)) {
            message = new ValidationResultImpl("Неверные имя пользователя или пароль");
        } else if (failed.getClass().equals(LockedException.class)) {
            message = new ValidationResultImpl("Данная учетная запись заблокирована. Обратитесь к администратору");
        } else if (failed.getClass().equals(DisabledException.class)) {
            message = new ValidationResultImpl("Неверные имя пользователя или пароль");
        } else {
            message = new ValidationResultImpl("Ошибка авторизации");
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), message);
    }
}
