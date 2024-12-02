package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.dto.LoginDto;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.UserRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.CookieUtils;
import com.onedreamus.project.global.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto getUserInfo(){
        Users user = getUser();

        log.info("[회원 정보 조회] 이메일 : {}", user.getEmail());
        return UserDto.from(user);
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
    public void logout(HttpServletResponse response) {
        String email = SecurityUtils.getEmail();
        Cookie deleteCookie = new Cookie("Authorization", "");
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        log.info("[회원 로그아웃] 이메일 : {}", email);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdraw(HttpServletResponse response) {
        Users user = getUser();
        Long socialId = user.getSocialId();

        // 소셜서비스와 연결 해제
        boolean isUnlinked = kakaoOAuth2Service.unlinkKakaoAccount(socialId);
        if (!isUnlinked){
            throw new UserException(ErrorCode.UNLINK_FAIL);
        }

        // DB 삭제 -> soft delete
        user.setDeleted(true);
        userRepository.save(user);

        // 기존 쿠키 삭제
        response.addCookie(CookieUtils.createDeleteCookie());
        log.info("[회원 탈퇴] 이메일 : {}, 시간 : {}, isDeleted : {}", user.getEmail(), LocalDateTime.now(), user.isDeleted());
    }


    /**
     * Users 획득
     */
    public Users getUser() {
        String email = SecurityUtils.getEmail();
        return getUserByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.NO_USER));

    }

    /**
     * email로 Optional<Users> 획득
     */
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
