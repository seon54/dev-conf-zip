package com.dev.conf.global.config;

import com.dev.conf.global.security.handler.JwtAuthenticationSuccessHandler;
import com.dev.conf.global.security.jwt.JwtUtil;
import com.dev.conf.global.security.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/",
                                        "/css/**",
                                        "/images/**",
                                        "/js/**",
                                        "/login/oauth2/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .oauth2Login(oauth ->
                        oauth.successHandler(new JwtAuthenticationSuccessHandler(objectMapper, jwtUtil))
                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                );
        return http.build();
    }

}
