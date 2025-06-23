package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.UserUpdateDTO;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.exception.UserNotFoundException;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.dto.response.AccessTokenDTO;

public interface UserService {
    User create(User user);

    UserDTO findByUsername(String username) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;

    UserDTO getUserInfo(User user);

    UserDTO updateUser(User user, UserUpdateDTO userUpdateDTO);

    AccessTokenDTO changePassword(User user, String oldPassword, String newPassword);
}
