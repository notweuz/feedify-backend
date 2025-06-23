package ru.ntwz.feedify.service;

import javax.crypto.SecretKey;

public interface JWTService {
    String generate(long id, String passwordHash);

    Long validate(String token);

    SecretKey getSigningKey();

    String extractPasswordHash(String token);
}
