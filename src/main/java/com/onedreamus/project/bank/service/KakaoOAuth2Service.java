package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.KakaoUnlinkResponse;
import com.onedreamus.project.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuth2Service {

    @Value("${spring.security.oauth2.client.registration.kakao.admin-key}")
    private String KAKAO_ADMIN_KEY;

    private final RestTemplate restTemplate;

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
        log.info("unlink 요청 kakaoId : {}", kakaoId);
        log.info("admin-key", KAKAO_ADMIN_KEY);
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
