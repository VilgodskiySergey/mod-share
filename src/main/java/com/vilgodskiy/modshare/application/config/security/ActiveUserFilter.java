package com.vilgodskiy.modshare.application.config.security;

import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@RequiredArgsConstructor
public class ActiveUserFilter implements Filter {

    private final UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        User activeUser = init();
        ActiveUserHolder.setActiveUser(activeUser);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private User init() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof String) {
            return userRepository
                    .findByLoginIgnoreCase((String) authentication.getPrincipal())
                    .orElse(null);
        }
        return null;
    }
}
