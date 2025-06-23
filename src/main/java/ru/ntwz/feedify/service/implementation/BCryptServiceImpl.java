package ru.ntwz.feedify.service.implementation;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.service.BCryptService;

@Service
public class BCryptServiceImpl implements BCryptService {
    @Override
    public String getHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean verify(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
