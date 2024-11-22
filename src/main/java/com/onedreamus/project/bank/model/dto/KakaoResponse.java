package com.onedreamus.project.bank.model.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        boolean isProfileAgreed = (boolean) kakaoAccount.get("profile_nickname_needs_agreement");
        if (isProfileAgreed){
            return kakaoAccount.get("email").toString();
        }

        return null;
    }

    @Override
    public String getName() {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        boolean isProfileAgreed = (boolean) kakaoAccount.get("profile_nickname_needs_agreement");
        if (isProfileAgreed){
            Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

            return properties.get("nickname").toString();
        }

        return null;
    }
}
