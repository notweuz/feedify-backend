package ru.ntwz.feedify.service;

public interface BCryptService {
    String getHash(String password);

    boolean verify(String password, String hash);
}
