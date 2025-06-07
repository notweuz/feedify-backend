package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.User;

public interface UserService {
    User create(User user);

    User findByUsername(String username) throws UserNotFoundException;
}
