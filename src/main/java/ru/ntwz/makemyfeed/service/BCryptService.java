package ru.ntwz.makemyfeed.service;

public interface BCryptService {
    String getHash(String password);

    boolean verify(String password, String hash);
}
