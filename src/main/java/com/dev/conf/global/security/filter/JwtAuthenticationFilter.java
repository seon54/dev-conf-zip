package com.dev.conf.global.security.filter;

import com.dev.conf.domain.user.exception.TokenInvalidException;
import com.dev.conf.global.security.jwt.JwtUtil;
import com.dev.conf.global.security.service.CustomOAuth2UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authentication";
    private static final String TOKEN_PREFIX = "Bearer";
    private final CustomOAuth2UserService oAuth2UserService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = StringUtils.delete(header, TOKEN_PREFIX).trim();
        String email = jwtUtil.extractEmail(token);
        OAuth2User user = oAuth2UserService.loadByEmail(email);

        if (!jwtUtil.validate(token, user)) {
            throw new TokenInvalidException();
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(user);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getAuthentication(OAuth2User user) {
        return new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
