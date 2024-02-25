package com.dev.conf.global.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token-expire-seconds}")
    private int accessTokenExpireSeconds;

    @Value("${jwt.refresh-token-expire-seconds}")
    private int refreshTokenExpireSeconds;

    public Map<String, String> generateJwt(OAuth2User oAuth2User) {
        String accessToken = generateToken(oAuth2User, accessTokenExpireSeconds);
        String refreshToken = generateToken(oAuth2User, refreshTokenExpireSeconds);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    private String generateToken(OAuth2User oAuth2User, int expireSeconds) {
        return Jwts.builder()
                .setClaims(Map.of("email", oAuth2User.getAttribute("email")))
                .setIssuer(issuer)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .setExpiration(new Date(new Date().getTime() + expireSeconds))
                .compact();
    }

}
