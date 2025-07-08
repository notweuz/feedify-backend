package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.UserUpdateDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.exception.UserNotFoundException;
import ru.ntwz.feedify.model.User;

public interface UserService {
    User create(User user);

    User save(User user);

    UserDto findByUsername(String username) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;

    UserDto getUserInfo(User user);

    UserDto getUserById(Long id);

    User updateUser(User user, UserUpdateDto userUpdateDTO);

    UserDto updateUserDto(User user, UserUpdateDto userUpdateDTO);

    AccessTokenDto changePassword(User user, String oldPassword, String newPassword);
}
