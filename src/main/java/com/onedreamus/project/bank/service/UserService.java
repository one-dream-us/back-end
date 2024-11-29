package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.dto.LoginDto;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.UserRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = customOAuth2User.getEmail();
        Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.NO_USER));
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
        Cookie deleteCookie = new Cookie("Authorization", "");
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);
    }

    public Users getUser() {
        String email = SecurityUtils.getEmail();
        return getUserByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.NO_USER));

    }

    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
