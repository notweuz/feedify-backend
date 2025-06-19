package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.request.UserUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;

public interface UserService {
    User create(User user);

    UserDTO findByUsername(String username) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;

    UserDTO getUserInfo(User user);

    UserDTO updateUser(User user, UserUpdateDTO userUpdateDTO);

    AccessTokenDTO changePassword(User user, String oldPassword, String newPassword);
}
