package com.onedreamus.project.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> redirect(@RequestParam("code") String code) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
