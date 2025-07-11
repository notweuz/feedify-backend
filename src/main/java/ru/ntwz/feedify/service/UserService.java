package ru.ntwz.feedify.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ntwz.feedify.dto.request.UserUpdateDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.exception.UserNotFoundException;
import ru.ntwz.feedify.model.User;

public interface UserService {
    User create(User user);

    User save(User user);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    UserDto findByUsername(String username) throws UserNotFoundException;

    User getByUsername(String username) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;

    UserDto getUserInfo();

    UserDto getUserById(Long id);

    User updateUser(UserUpdateDto userUpdateDTO);

    UserDto updateUserDto(UserUpdateDto userUpdateDTO);

    AccessTokenDto changePassword(String oldPassword, String newPassword);
}
