package ru.ntwz.makemyfeed.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.exception.TokenNotProvidedException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.AuthorizationService;

import java.util.Objects;

@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationInterceptor(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (Objects.equals(request.getMethod(), HttpMethod.OPTIONS.name())) return true;
        if (Objects.equals(request.getMethod(), HttpMethod.GET.name()) && request.getRequestURI().matches("^/posts/\\d+$")) return true; // разрешить доступ к просмотру постов без авторизации

        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) throw new TokenNotProvidedException("Authorization token not provided");
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);

        User user = authorizationService.authUser(accessToken);

        request.setAttribute(AttributesConstants.USER, user);
        return true;
    }
}