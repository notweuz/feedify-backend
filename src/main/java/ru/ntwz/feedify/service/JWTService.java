package ru.ntwz.feedify.service;

import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

public interface JWTService {
    String extractUsername(String token);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

//    String generate(long id, String passwordHash);
//
//    Long validate(String token);
//
//    SecretKey getSigningKey();
//
//    String extractPasswordHash(String token);
//
//    String extractUsername(String token);
}
