package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.dto.KakaoResponse;
import com.onedreamus.project.bank.model.dto.OAuth2Response;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.UserRepository;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        System.out.println("OAuth2Response >>> " + oAuth2User);


        String nickname = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        log.info("OAuth2Response >>> {}", nickname);

        Optional<Users> userOptional = userRepository.findByEmail(oAuth2Response.getEmail());

        if (userOptional.isEmpty()) {
            Users newUser = Users.builder()
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .role("ROLE_USER")
                .build();

            userRepository.save(newUser);

            UserDto userDto = UserDto.builder()
                .name(newUser.getName())
                .email(oAuth2Response.getEmail())
                .role("ROLE_USER")
                .nickname(nickname)
                .build();

            return new CustomOAuth2User(userDto);
        } else {
            Users existUser = userOptional.get();
            existUser.setEmail(oAuth2Response.getEmail());
            existUser.setName(oAuth2Response.getName());

            userRepository.save(existUser);

            UserDto userDto = UserDto.builder()
                .email(existUser.getEmail())
                .name(existUser.getName())
                .role(existUser.getRole())
                .nickname(existUser.getNickname())
                .build();

            return new CustomOAuth2User(userDto);
        }


    }
}
