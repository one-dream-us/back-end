package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.dto.LoginDto;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.UserRepository;
import com.onedreamus.project.bank.service.KakaoOAuth2Service;
import com.onedreamus.project.bank.service.UserService;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Controller", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    /**
     *토큰으로 유저 데이터를 잘 반환하는지 테스트하기위한 API
     */
    @GetMapping("/info")
    @Operation(
        summary = "유저 데이터 조회",
        description = "유저데이터 조회 API")
    public ResponseEntity<UserDto> getUserInfo(){
        UserDto userDto = userService.getUserInfo();
        return ResponseEntity.ok(userDto);
    }

    /**
     * 회원가입
     * 소셜로그인만 진행하기로 함.
     * deprecated
     */
    @Deprecated
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto){
        userService.join(joinDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        userService.logout(response);
        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 - soft delete + 소셜 서비스 연결 해제")
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawMembership(HttpServletResponse response){
        userService.withdraw(response);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    /**
     * 회원가입/로그인 세로운 프로세스 MVP 이후 적용
     */
//    @PostMapping("/social/join")
//    public ResponseEntity<String> test(@RequestBody Map<String, String> requestBody, HttpServletResponse response) {
//        String tempToken = requestBody.get("token");
//
//        // 만료된 토큰인지 점검 필요
//        if (jwtUtil.isExpired(tempToken)){
//            throw new UserException(ErrorCode.TOKEN_EXPIRED);
//        }
//
//        String username = jwtUtil.getUsername(tempToken);
//        String email = jwtUtil.getEmail(tempToken);
//        String role = jwtUtil.getRole(tempToken);
//
//        Users user = Users.builder()
//            .name(username)
//            .email(email)
//            .role(role)
//            .provider("kakao")
//            .deleted(false)
//            .build();
//
//        userRepository.save(user);
//
//        // cookie 발급
//        String token = jwtUtil.createJwt(username, email, role, true);
//        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.create(token));
//        // response 쿠키에 토큰 추가
//        return ResponseEntity.ok("회원가입 성공!!");
//    }
//
//    @PostMapping("/social/unlink")
//    public ResponseEntity<String> unlinkKakao(@RequestBody Map<String, String> requestBody) {
//        String token = requestBody.get("token");
//        log.info("token from frontend : {}", token);
//        boolean isUnlinked = kakaoOAuth2Service.unlinkKakaoAccount(jwtUtil.getSocialId(token));
//        if (!isUnlinked){
//            log.info("소셜로그인 연결 해제 실패!!");
//            throw new UserException(ErrorCode.UNLINK_FAIL);
//        }
//
//        log.info("소셜로그인 연결 해제 성공!!");
//        return ResponseEntity.ok("소셜로그인 연결 해제 성공");
//    }

}
