package com.dev.conf.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockOAuth2User {

    String username() default "user";
    String email() default "user@test.com";
    String provider() default "oauthProvider";
    String providerId() default "oauthProviderId";
}
