package com.onedreamus.project.global.util;

import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import com.onedreamus.project.bank.model.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getEmail();
    }

    public Users getUser(CustomUserDetails customUserDetails) {
        return customUserDetails.getUser();
    }
}
