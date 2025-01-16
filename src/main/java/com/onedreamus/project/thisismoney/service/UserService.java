package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.config.oauth2.UserChecker;
import com.onedreamus.project.global.exception.LoginException;
import com.onedreamus.project.thisismoney.exception.UserException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.global.config.jwt.TokenType;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final CookieUtils cookieUtils;
    private final ScrapService scrapService;
    private final ContentHistoryService contentHistoryService;
    private final UserChecker userChecker;
    private final JWTUtil jwtUtil;
    private final NoteService noteService;

    public void saveUser(Users user) {
        userRepository.save(user);
    }

    /**
     * 유저 상세 정보 조회
     */
    public UserInfoDto getUserInfo(Users user) {

        log.info("[회원 정보 조회] 이메일 : {}", user.getEmail());
        return UserInfoDto.from(user);
    }

    /**
     * 회원가입
     */
    public void join(JoinDto joinDto) {

        // 이메일 중복 확인
        boolean isEmailRegistered = userRepository.existsByEmail(joinDto.getEmail());
        if (isEmailRegistered) {
            throw new UserException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 새로운 유저 생성
        Users newUsers = Users.from(joinDto);
        userRepository.save(newUsers);
    }

    /**
     * 로그아웃
     */
    public void logout(HttpServletResponse response, Users user) {
        String email = user.getEmail();

        // 기존 쿠키 삭제
        List<String> allTokenType = Arrays.stream(TokenType.values())
                .map(TokenType::getName)
                .toList();
        cookieUtils.deleteAllCookie(response, allTokenType);

        log.info("[회원 로그아웃] 이메일 : {}", email);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdraw(HttpServletResponse response, Users user) {
        Long socialId = user.getSocialId();

        // 소셜서비스와 연결 해제
        boolean isUnlinked = kakaoOAuth2Service.unlinkKakaoAccount(socialId);
        if (!isUnlinked) {
            throw new UserException(ErrorCode.UNLINK_FAIL);
        }

        // DB 삭제 -> soft delete
        user.setDeleted(true);
        user.setRefreshToken(null);
        userRepository.save(user);

        // 기존 쿠키 삭제
        List<String> allTokenType = TokenType.getAllTokenName();
        cookieUtils.deleteAllCookie(response, allTokenType);

        // 기존 데이터 삭제
        scrapService.deleteAllScraps(user);

        // 컨텐츠 조회 수 삭제
        contentHistoryService.deleteAllHistory(user);

        log.info("[회원 탈퇴] 이메일 : {}, 시간 : {}, isDeleted : {}", user.getEmail(), LocalDateTime.now(), user.isDeleted());
    }

    /**
     * 소셜로그인 회원가입
     */
    public List<String> joinSocial(HttpServletRequest request) {

        String verifyToken = findTokenFromCookie(request.getCookies(), TokenType.VERIFY_TOKEN);

        if (verifyToken == null) {
            throw new UserException(ErrorCode.TOKEN_NULL);
        }

        String email = jwtUtil.getEmail(verifyToken);
        UserCheckDto userCheck = userChecker.get(email);

        List<String> tokenCookies = new ArrayList<>();
        String accessToken = jwtUtil.createJwt(userCheck.getName(), userCheck.getEmail(), userCheck.getRole(), true, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJwt(userCheck.getName(), userCheck.getEmail(), userCheck.getRole(), true, TokenType.REFRESH_TOKEN);

        Users newUser = Users.from(userCheck, refreshToken);
        userRepository.save(newUser);

        tokenCookies.addAll(cookieUtils.createAllCookies(TokenType.ACCESS_TOKEN.getName(), accessToken));
        tokenCookies.addAll(cookieUtils.createAllCookies(TokenType.REFRESH_TOKEN.getName(), refreshToken));
        tokenCookies.add(cookieUtils.createDeleteCookie(TokenType.VERIFY_TOKEN.getName(), "localhost"));
        tokenCookies.add(cookieUtils.createDeleteCookie(TokenType.VERIFY_TOKEN.getName(), "thisismoney.site"));

        return tokenCookies;
    }

    /**
     * 소셜 연동 해제
     */
    public List<String> unlinkSocial(HttpServletRequest request) {
        // 쿠키 조회
        String verifyToken = findTokenFromCookie(request.getCookies(), TokenType.VERIFY_TOKEN);

        if (verifyToken == null) {
            throw new LoginException(ErrorCode.TOKEN_NULL);
        }

        String email = jwtUtil.getEmail(verifyToken);
        Long socialId = userChecker.getSocialId(email);
        kakaoOAuth2Service.unlinkKakaoAccount(socialId);

        List<String> tokenCookies = new ArrayList<>();
        cookieUtils.createDeleteCookie(TokenType.VERIFY_TOKEN.getName(), "localhost");
        cookieUtils.createDeleteCookie(TokenType.VERIFY_TOKEN.getName(), "thisismoney.site");

        return tokenCookies;
    }

    private String findTokenFromCookie(Cookie[] cookies, TokenType tokenType) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenType.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public FirstQuizAttemptResponse checkFirstAttempt(Users user) {
        return new FirstQuizAttemptResponse(user.isQuizAttempt());
    }
}
