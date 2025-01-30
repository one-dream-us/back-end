package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.thisismoney.model.dto.CustomOAuth2User;
import com.onedreamus.project.thisismoney.model.dto.UserCheckDto;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.config.jwt.TokenType;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.cookie.domain.server}")
    private String SERVER_DOMAIN;

    @Value("${spring.cookie.domain.local}")
    private String LOCAL_DOMAIN;

    private final String REDIRECT_URL = "redirectUrl";
    private final String JOIN_URL = "joinUrl";
    private final String REFERER = "Referer";

    private final JWTUtil jwtUtil;
    private final UserChecker userChecker;
    private final UserRepository userRepository;
    private final CookieUtils cookieUtils;
    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

//        String homeAddress = request.getHeader(REFERER);
        HttpSession session = request.getSession(false);
        String redirectUrl = (String) session.getAttribute(REDIRECT_URL);
        String joinUrl = (String) session.getAttribute(JOIN_URL);
//        String domain = getDomain(redirectUrl);

        log.info("redirectUrl : {}, joinUrl : {}", redirectUrl, joinUrl);

        //OAuth2User
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Users user = customOAuth2User.getUser();
        String username = user.getName();
        String email = user.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 새로운 유저인 경우
        if (!customOAuth2User.isUser()) {

            userChecker.add(user.getEmail(), UserCheckDto.from(user));

            String tempToken = jwtUtil.createTempJwt(email, user.getProvider());

            List<String> cookies = cookieUtils.createAllCookies(TokenType.VERIFY_TOKEN.getName(), tempToken);
            for (String cookie : cookies) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie);
            }

            response.sendRedirect(joinUrl + "?isNewUser=true&timestamp" + System.currentTimeMillis());
            return;
        }

        String accessToken = jwtUtil.createJwt(username, email, role, true, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJwt(username, email, role, true,
            TokenType.REFRESH_TOKEN);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // 토큰 발급
        issueToken(response, accessToken, refreshToken);

        response.sendRedirect(redirectUrl);
    }

    private String getDomain(String redirectUrl) {
        int startIdx = redirectUrl.indexOf('/', 10);
        return redirectUrl.substring(0, startIdx + 1);
    }

    /**
     * 토큰 발급
     * - response 에 토큰 추가
     */
    private void issueToken(HttpServletResponse response, String accessToken, String refreshToken) {

        // 서버용 토큰 발급
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), accessToken, SERVER_DOMAIN));
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.create(TokenType.REFRESH_TOKEN.getName(), refreshToken, SERVER_DOMAIN));

        // 로컬 테스트용 토큰 발급
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), accessToken, LOCAL_DOMAIN));
        response.addHeader(HttpHeaders.SET_COOKIE,
                cookieUtils.create(TokenType.REFRESH_TOKEN.getName(), refreshToken, LOCAL_DOMAIN));
    }

}
