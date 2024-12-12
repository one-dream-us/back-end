package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.exception.UserException;
import com.onedreamus.project.thisismoney.model.dto.KakaoUnlinkResponse;
import com.onedreamus.project.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoOAuth2Service {

    private final String KAKAO_ADMIN_KEY;

    private final RestTemplate restTemplate;

    public KakaoOAuth2Service(@Value("${spring.security.oauth2.client.registration.kakao.admin-key}") String kakaoAdminKey,
                              RestTemplate restTemplate) {
        this.KAKAO_ADMIN_KEY = kakaoAdminKey;
        this.restTemplate = restTemplate;
    }

    public boolean unlinkKakaoAccount(Long kakaoId) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);

        // 요청 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", String.valueOf(kakaoId));

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            // 카카오 API 호출
            ResponseEntity<KakaoUnlinkResponse> response = restTemplate.exchange(
                unlinkUrl,
                HttpMethod.POST,
                request,
                KakaoUnlinkResponse.class
            );

            long userKakaoId = response.getBody().getId();
            return userKakaoId == kakaoId;
        } catch (Exception e) {
            log.error("[kakao unlink error] : {}", e.getMessage());
            throw new UserException(ErrorCode.UNLINK_FAIL);
        }
    }
}
