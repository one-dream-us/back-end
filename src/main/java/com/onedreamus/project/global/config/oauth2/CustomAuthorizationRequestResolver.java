package com.onedreamus.project.global.config.oauth2;

import com.onedreamus.project.global.exception.FilterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
@Slf4j
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;
    private final HttpServletRequest request;

    public CustomAuthorizationRequestResolver(
        ClientRegistrationRepository clientRegistrationRepository,
        HttpServletRequest request) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, "/oauth2/authorization");
        this.request = request;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest servletRequest) {
        OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(servletRequest);
        return addPreviousPageToRequest(originalRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest servletRequest, String registrationId) {
        OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(servletRequest, registrationId);
        return addPreviousPageToRequest(originalRequest);
    }

    private OAuth2AuthorizationRequest addPreviousPageToRequest(OAuth2AuthorizationRequest originalRequest) {
        if (originalRequest == null) {
            return null;
        }

        String redirectUrl = request.getParameter("redirectUrl");
        log.info("Redirect URL : {}", redirectUrl);

        HttpSession session = request.getSession();
        session.setAttribute("redirectUrl", redirectUrl);

        return OAuth2AuthorizationRequest.from(originalRequest)
            .build();
    }
}
