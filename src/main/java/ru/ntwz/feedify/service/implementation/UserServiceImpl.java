package ru.ntwz.feedify.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.dto.mapper.UserMapper;
import ru.ntwz.feedify.dto.request.UserUpdateDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.exception.InvalidPasswordException;
import ru.ntwz.feedify.exception.UserNotFoundException;
import ru.ntwz.feedify.exception.UserWithSameNameAlreadyExistsException;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.UserRepository;
import ru.ntwz.feedify.service.BCryptService;
import ru.ntwz.feedify.service.JWTService;
import ru.ntwz.feedify.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptService bCryptService;
    private final JWTService jwtService;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptService bCryptService, JWTService jwtService, CustomUserDetailsServiceImpl customUserDetailsService) {
        this.userRepository = userRepository;
        this.bCryptService = bCryptService;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    @Caching(put = {
        @CachePut(value = "users", key = "#user.username"),
        @CachePut(value = "usersById", key = "#user.id")
    })
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserWithSameNameAlreadyExistsException("User with username '" + user.getUsername() + "' already exists");

        log.info("Creating user: {}", user.getUsername());

        return userRepository.save(user);
    }

    @Override
    @Caching(put = {
        @CachePut(value = "users", key = "#user.username"),
        @CachePut(value = "usersById", key = "#user.id")
    })
    public User save(User user) {
        log.info("Saving user: {}", user.getUsername());

        return userRepository.save(user);
    }

    @Override
    public CustomUserDetailsServiceImpl userDetailsService() {
        return customUserDetailsService;
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Override
    public UserDto findByUsername(String username) throws UserNotFoundException {
        User user = getByUsername(username);

        log.info("Found user: {}", user.getUsername());

        return UserMapper.toDTO(user);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public User getByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        log.info("Retrieved user by username: {}", username);

        return user;
    }

    @Override
    @Cacheable(value = "usersById", key = "#id")
    public User getById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + id + "' not found"));
        log.info("Retrieved user by ID: {}", id);
        return user;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = getById(userId);
        log.info("Retrieved user by ID: {}", userId);
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDto getUserInfo() {
        User user = getCurrentUser();
        log.info("Retrieved user info for user: {}", user.getUsername());
        return UserMapper.toDTO(user);
    }

    @Override
    @Caching(put = {
        @CachePut(value = "users", key = "#user.username"),
        @CachePut(value = "usersById", key = "#user.id")
    })
    public User updateUser(UserUpdateDto userUpdateDTO) {
        User user = getCurrentUser();
        if (userUpdateDTO.getDisplayName() != null) {
            user.setDisplayName(userUpdateDTO.getDisplayName());
        }
        if (userUpdateDTO.getUsername() != null) {
            if (userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
                throw new UserWithSameNameAlreadyExistsException("User with username '" + userUpdateDTO.getUsername() + "' already exists");
            }
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getDescription() != null) {
            user.setDescription(userUpdateDTO.getDescription());
        }
        log.info("Updated user info for user: {}", user.getUsername());
        return userRepository.save(user);
    }


    @Override
    public UserDto updateUserDto(UserUpdateDto userUpdateDTO) {
        User updated = updateUser(userUpdateDTO);
        return UserMapper.toDTO(updated);
    }

    @Override
    public AccessTokenDto changePassword(String oldPassword, String newPassword) {
        User user = getCurrentUser();
        return doChangePassword(user, oldPassword, newPassword);
    }

    @Caching(evict = {
        @CacheEvict(value = "users", key = "#user.username"),
        @CacheEvict(value = "usersById", key = "#user.id")
    })
    private AccessTokenDto doChangePassword(User user, String oldPassword, String newPassword) {
        if (!bCryptService.verify(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Wrong old password provided");
        }
        user.setPassword(bCryptService.getHash(newPassword));
        userRepository.save(user);
        String newToken = jwtService.generateToken(user);
        log.info("User {} changed his password", user.getUsername());
        return new AccessTokenDto(newToken);
    }
}
