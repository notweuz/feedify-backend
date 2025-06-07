package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.User;

public interface UserService {
    User create(User user);

    User getByUsername(String username) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;
}
