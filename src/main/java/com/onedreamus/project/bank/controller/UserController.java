package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.JoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {



    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto){

        return ResponseEntity.ok("로그인 성공");
    }

}
