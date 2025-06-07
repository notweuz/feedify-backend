package ru.ntwz.makemyfeed.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.config.JWTConfig;
import ru.ntwz.makemyfeed.exception.NotAuthorizedException;
import ru.ntwz.makemyfeed.service.JWTService;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
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
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        if (claimsJws.getPayload().getId() == null) throw new NotAuthorizedException("Invalid token: no user ID found");
        if (claimsJws.getPayload().getSubject() == null) throw new NotAuthorizedException("Invalid token: no password hash found");

        return Long.parseLong(claimsJws.getPayload().getId());
    }

    @Override
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }
}
