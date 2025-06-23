package ru.ntwz.feedify.util;

import java.util.Random;

public class RandUtils {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LINK_LENGTH = 16;

    public static String generateUniqueLink() {
        Random random = new Random();
        StringBuilder link = new StringBuilder(LINK_LENGTH);
        for (int i = 0; i < LINK_LENGTH; i++) {
            link.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return link.toString();
    }
}