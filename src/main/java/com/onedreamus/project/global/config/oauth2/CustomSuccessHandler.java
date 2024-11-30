package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserChecker userChecker;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getName();
        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        /**
         * 새로운 회원가입/로그인 프로세스 MVP 이후 적용
         */
        // 새로운 유저인 경우
//        if (!userChecker.isUser(email)) {
//            String tempToken =
//                jwtUtil.createTempJwt(username, email, role, userChecker.getSocialId(email));
//            response.sendRedirect("http://localhost:3000/verify?token=" + tempToken);
//            return;
//        }

        String token = jwtUtil.createJwt(username, email, role, true);
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.create(token));
        response.sendRedirect("http://localhost:3000");
    }

}
