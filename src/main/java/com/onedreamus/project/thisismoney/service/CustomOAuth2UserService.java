package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.CustomOAuth2User;
import com.onedreamus.project.thisismoney.model.dto.GoogleResponse;
import com.onedreamus.project.thisismoney.model.dto.KakaoResponse;
import com.onedreamus.project.thisismoney.model.dto.OAuth2Response;
import com.onedreamus.project.thisismoney.model.dto.UserDto;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.config.oauth2.UserChecker;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserChecker userChecker;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String nickname = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        Optional<Users> userOptional = userRepository.findByEmail(oAuth2Response.getEmail());

        UserDto userDto;
        Users user;
        boolean isUser;
        if (userOptional.isEmpty()) { // 새로운 유저인 경우

            user = Users.builder()
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .provider(oAuth2Response.getProvider())
                .nickname(nickname)
                .role("ROLE_USER")
                .socialId(oAuth2Response.getSocialId())
                .quizAttempt(false)
                .build();

            isUser = false;

        } else if (userOptional.get().isDeleted()) { // soft delete된 유저의 재가입인 경우

            user = userOptional.get();
            user.setDeleted(false);
            user.setName(oAuth2Response.getName());
            user.setNickname(nickname);
            user.setSocialId(oAuth2Response.getSocialId());
            user.setProvider(oAuth2Response.getProvider());

            isUser = false;

        } else { // 기존 유저인 경우(단순 로그인)
            user = userOptional.get();
            user.setName(oAuth2Response.getName());
            user.setSocialId(oAuth2Response.getSocialId());

            isUser = true;
        }

        return new CustomOAuth2User(user, isUser);
    }
}
