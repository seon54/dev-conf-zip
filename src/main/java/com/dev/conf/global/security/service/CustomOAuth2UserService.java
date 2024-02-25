package com.dev.conf.global.security.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.user.repository.UserRepository;
import com.dev.conf.global.security.model.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        User user = getUser(provider, oAuth2User);
        Map<String, Object> oAuth2Attributes = getOAuth2Attributes(attributes);
        return new CustomOAuth2User(user, oAuth2Attributes);
    }

    private User getUser(String provider, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = oAuth2User.getAttribute("sub");
        String email = attributes.get("email").toString();
        return userRepository.findByEmail(email).orElseGet(() -> {
            String username = (String) attributes.get("name");
            return userRepository.save(new User(username, email, provider, providerId));
        });
    }

    private static Map<String, Object> getOAuth2Attributes(Map<String, Object> attributes) {
        Map<String, Object> oAuth2Attributes = new HashMap<>();
        oAuth2Attributes.put("id", attributes.get("sub"));
        oAuth2Attributes.put("email", attributes.get("email").toString());
        return oAuth2Attributes;
    }

}
