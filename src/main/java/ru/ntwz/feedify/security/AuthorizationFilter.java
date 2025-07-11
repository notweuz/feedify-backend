package ru.ntwz.feedify.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ntwz.feedify.constant.AuthorizationConstant;
import ru.ntwz.feedify.dto.response.ApiError;
import ru.ntwz.feedify.exception.NotAuthorizedException;
import ru.ntwz.feedify.service.JWTService;
import ru.ntwz.feedify.service.implementation.CustomUserDetailsServiceImpl;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final ObjectMapper mapper;

    @Autowired
    public AuthorizationFilter(JWTService jwtService, CustomUserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

        this.mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AuthorizationConstant.HEADER_NAME);

        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(AuthorizationConstant.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(AuthorizationConstant.TOKEN_PREFIX.length());
        Long userId = null;
        try {
            userId = jwtService.extractUserId(jwt);

            if (userId == null) {
                throw new NotAuthorizedException("Invalid JWT token: user id not found");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                if (!jwtService.validateToken(jwt, userDetails)) {
                    throw new NotAuthorizedException("Invalid JWT token");
                }

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
            filterChain.doFilter(request, response);
        } catch (NotAuthorizedException ex) {
            ApiError apiError = new ApiError();
            apiError.setErrors(Collections.singletonList(ex.toString()));
            apiError.setReason("Not authorized to access this resource");
            apiError.setStatus(HttpStatus.UNAUTHORIZED.name());
            apiError.setMessage(ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            try {
                response.getWriter().write(mapper.writeValueAsString(apiError));
            } catch (IOException e) {
                log.error("Error writing response: {}", e.getMessage());
            }
        }
    }
}
