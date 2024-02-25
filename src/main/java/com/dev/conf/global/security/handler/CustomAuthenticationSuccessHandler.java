package com.dev.conf.global.security.handler;

import com.dev.conf.global.security.jwt.JwtUtil;
import com.dev.conf.global.security.model.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();

        Map<String, String> tokenMap = jwtUtil.generateJwt(user);
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("accessToken", tokenMap.get("accessToken"));
        queryParams.add("refreshToken", tokenMap.get("refreshToken"));

        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("localhost")
                .port(8080)
                .path("/")
                .queryParams(queryParams)
                .build()
                .toString();

        getRedirectStrategy().sendRedirect(request, response, uri);
    }
}
