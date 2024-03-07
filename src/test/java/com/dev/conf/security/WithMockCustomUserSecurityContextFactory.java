package com.dev.conf.security;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.global.security.model.CustomOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockOAuth2User> {

    @Override
    public SecurityContext createSecurityContext(WithMockOAuth2User mockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = new User(mockCustomUser.username(), mockCustomUser.email(), mockCustomUser.provider(), mockCustomUser.providerId());
        CustomOAuth2User oAuth2User = new CustomOAuth2User(user, new HashMap<>());
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(oAuth2User, "", AuthorityUtils.createAuthorityList("ROLE_USER"));
        context.setAuthentication(auth);
        return context;
    }
}
