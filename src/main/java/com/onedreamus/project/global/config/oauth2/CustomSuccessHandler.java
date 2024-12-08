package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.UserRepository;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.config.jwt.TokenType;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String REDIRECT_URL = "redirectUrl";
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

        //OAuth2User
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Users user = customOAuth2User.getUser();
        String username = user.getName();
        String email = user.getEmail();

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

        String accessToken = jwtUtil.createJwt(username, email, role, true, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJwt(username, email, role, true,
            TokenType.REFRESH_TOKEN);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        response.addHeader(HttpHeaders.SET_COOKIE,
            cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), accessToken));
        response.addHeader(HttpHeaders.SET_COOKIE,
            cookieUtils.create(TokenType.REFRESH_TOKEN.getName(), refreshToken));

        String homeAddress = request.getHeader(REFERER);
        String servletPath = request.getServletPath();

        HttpSession session = request.getSession(false);
        String redirectUrl = (String) session.getAttribute(REDIRECT_URL);
        String domain = getDomain(redirectUrl);

        log.info("homeAddress : {}, servletPath : {}, redirectUrl : {}, domain : {}", homeAddress, servletPath, redirectUrl, domain);

        // 회원가입인 경우
        if (homeAddress == null) {
            response.sendRedirect(domain);
        } else { // 로그인인 경우
            response.sendRedirect(redirectUrl);
        }
    }

    private String getDomain(String redirectUrl) {
        int startIdx = redirectUrl.indexOf('/', 10);
        return redirectUrl.substring(0, startIdx + 1);
    }

}
