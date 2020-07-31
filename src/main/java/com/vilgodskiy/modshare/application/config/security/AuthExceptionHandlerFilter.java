package com.vilgodskiy.modshare.application.config.security;

import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vilgodskiy.modshare.application.exception.AuthExecutionConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class AuthExceptionHandlerFilter extends GenericFilterBean {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (HttpStatusCodeException e) {
            SecurityContextHolder.clearContext();
            handle((HttpServletResponse) response, e, e.getStatusCode());
        } catch (AuthExecutionConflictException e) {
            handle((HttpServletResponse) response, e, HttpStatus.BAD_REQUEST);
        }
    }

    private void handle(final HttpServletResponse response,
                        final Exception exception,
                        HttpStatus status)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String message =
                isInteger(exception.getMessage().substring(0, 2))
                        ? exception.getMessage().substring(4)
                        : exception.getMessage();
        ValidationResult errorMessage = new ValidationResultImpl(message);
        mapper.writeValue(response.getWriter(), errorMessage);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
