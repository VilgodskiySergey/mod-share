package com.vilgodskiy.modshare.application.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vilgodskiy.modshare.application.config.security.domain.CurrentUser;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Vilgodskiy Sergey 29.07.2020
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_PARAM = "access_token";

    private final AccountService accountService;
    private final JwtTokenService jwtTokenService;
    private final TokenService tokenService;
    private final AuthLogoutHandler logoutHandler;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterBefore(new AuthExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), mapper, jwtTokenService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenService, tokenService, userRepository))
                .addFilterAfter(new ActiveUserFilter(userRepository), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .addLogoutHandler(logoutHandler)
                .logoutRequestMatcher(
                        new AntPathRequestMatcher("/logout")
                )
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/ui",
                        "/swagger-resources", "/configuration/security", "/swagger-ui.html",
                        "/webjars/**", "/swagger-resources/**", "/api/discovery/oauth-server").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> accountService.getByLogin(login)
                .map(CurrentUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином='" + login + "' не был найден"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> response.setStatus(HttpStatus.OK.value());
    }
}