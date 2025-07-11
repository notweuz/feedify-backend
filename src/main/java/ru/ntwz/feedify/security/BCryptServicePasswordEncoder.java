package ru.ntwz.feedify.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ntwz.feedify.service.BCryptService;

@Component
public class BCryptServicePasswordEncoder implements PasswordEncoder {
    private final BCryptService bCryptService;

    @Autowired
    public BCryptServicePasswordEncoder(BCryptService bCryptService) {
        this.bCryptService = bCryptService;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptService.getHash(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bCryptService.verify(rawPassword.toString(), encodedPassword);
    }
}
