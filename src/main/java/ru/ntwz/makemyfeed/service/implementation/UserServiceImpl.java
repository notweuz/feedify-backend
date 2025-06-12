package ru.ntwz.makemyfeed.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.UserMapper;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.exception.UserWithSameNameAlreadyExistsException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.UserRepository;
import ru.ntwz.makemyfeed.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) throw new UserWithSameNameAlreadyExistsException("User with username '" + user.getUsername() + "' already exists");

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
}
