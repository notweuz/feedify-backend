package ru.ntwz.feedify.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.config.JWTConfig;
import ru.ntwz.feedify.exception.NotAuthorizedException;
import ru.ntwz.feedify.service.JWTService;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {

    private final JWTConfig jwtConfig;

    public JWTServiceImpl(@Autowired JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String generate(long id, String passwordHash) {
        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + jwtConfig.getExpiration() * 1000);

        return Jwts.builder()
                .id(String.valueOf(id))
                .subject(passwordHash)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public Long validate(String token) throws NotAuthorizedException {
        if (token == null || token.trim().isEmpty()) {
            throw new NotAuthorizedException("Token is null or empty");
        }

        if (token.chars().filter(ch -> ch == '.').count() != 2) {
            throw new NotAuthorizedException("Invalid token format: Token must contain exactly two periods");
        }

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        if (claimsJws.getPayload().getId() == null) throw new NotAuthorizedException("Invalid token: no user ID found");
        if (claimsJws.getPayload().getSubject() == null)
            throw new NotAuthorizedException("Invalid token: no password hash found");

        return Long.parseLong(claimsJws.getPayload().getId());
    }

    @Override
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    @Override
    public String extractPasswordHash(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload().getSubject();
    }
}
