package ru.ntwz.feedify.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.config.JWTConfig;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.service.JWTService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {
    private final JWTConfig jwtConfig;

    @Autowired
    public JWTServiceImpl(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("username", customUserDetails.getUsername());
            claims.put("displayName", customUserDetails.getDisplayName());
            claims.put("passwordHash", customUserDetails.getPassword());
        }
        return generateToken(claims, userDetails);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        Long userIdFromToken = extractUserId(token);
        String passwordHashFromToken = extractPasswordHash(token);
        if (userDetails instanceof User user) {
            return userIdFromToken.equals(user.getId())
                && !isTokenExpired(token)
                && user.getPassword().equals(passwordHashFromToken);
        }
        return false;
    }

    @Override
    public Long extractUserId(String token) {
        Object idClaim = extractClaim(token, claims -> claims.get("id"));
        if (idClaim instanceof Integer) {
            return ((Integer) idClaim).longValue();
        } else if (idClaim instanceof Long) {
            return (Long) idClaim;
        } else if (idClaim instanceof String) {
            try {
                return Long.parseLong((String) idClaim);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid id in JWT token");
            }
        }
        throw new RuntimeException("User id not found in JWT token");
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtConfig.getExpiration() * 1000);

        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey()).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return claimsJws.getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    private String extractPasswordHash(String token) {
        Object hash = extractClaim(token, claims -> claims.get("passwordHash"));
        return hash != null ? hash.toString() : null;
    }
}
