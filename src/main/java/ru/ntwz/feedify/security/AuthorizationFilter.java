package ru.ntwz.feedify.security;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ntwz.feedify.constant.AuthorizationConstant;
import ru.ntwz.feedify.exception.NotAuthorizedException;
import ru.ntwz.feedify.service.JWTService;
import ru.ntwz.feedify.service.implementation.CustomUserDetailsServiceImpl;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthorizationFilter(JWTService jwtService, CustomUserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }
}
