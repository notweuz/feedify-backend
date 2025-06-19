package ru.ntwz.makemyfeed.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.UserMapper;
import ru.ntwz.makemyfeed.dto.request.UserUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.exception.UserWithSameNameAlreadyExistsException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.UserRepository;
import ru.ntwz.makemyfeed.service.UserService;
import ru.ntwz.makemyfeed.service.BCryptService;
import ru.ntwz.makemyfeed.service.JWTService;
import ru.ntwz.makemyfeed.exception.InvalidPasswordException;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptService bCryptService;
    private final JWTService jwtService;

    public UserServiceImpl(@Autowired UserRepository userRepository, @Autowired BCryptService bCryptService, @Autowired JWTService jwtService) {
        this.userRepository = userRepository;
        this.bCryptService = bCryptService;
        this.jwtService = jwtService;
    }

    @Override
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserWithSameNameAlreadyExistsException("User with username '" + user.getUsername() + "' already exists");

        log.info("Creating user: {}", user.getUsername());

        return userRepository.save(user);
    }

    @Override
    public UserDTO findByUsername(String username) throws UserNotFoundException {
        User user = getByUsername(username);

        log.info("Found user: {}", user.getUsername());

        return UserMapper.toDTO(user);
    }

    @Override
    public User getByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        log.info("Retrieved user by username: {}", username);

        return user;
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + id + "' not found"));

        log.info("Retrieved user by ID: {}", id);

        return user;
    }

    @Override
    public UserDTO getUserInfo(User user) {
        getById(user.getId());

        log.info("Retrieved user info for user: {}", user.getUsername());

        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getDisplayName() != null) {
            user.setDisplayName(userUpdateDTO.getDisplayName());
        }
        if (userUpdateDTO.getUsername() != null) {
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getDescription() != null) {
            user.setDescription(userUpdateDTO.getDescription());
        }

        log.info("Updated user info for user: {}", user.getUsername());

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public AccessTokenDTO changePassword(User user, String oldPassword, String newPassword) {
        if (!bCryptService.verify(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Wrong old password provided");
        }
        user.setPassword(bCryptService.getHash(newPassword));
        userRepository.save(user);
        String newToken = jwtService.generate(user.getId(), user.getPassword());
        log.info("User {} changed his password", user.getUsername());
        return new AccessTokenDTO(newToken);
    }
}
