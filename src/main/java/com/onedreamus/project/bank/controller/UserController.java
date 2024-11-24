package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.JoinDto;
import com.onedreamus.project.bank.model.dto.LoginDto;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     *토큰으로 유저 데이터를 잘 반환하는지 테스트하기위한 API
     */
    @GetMapping("/test")
    public ResponseEntity<UserDto> test(){
        UserDto userDto = userService.test();
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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){
        userService.logout(response);
        return ResponseEntity.ok("로그아웃 성공");
    }

}
