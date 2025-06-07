package ru.ntwz.makemyfeed.service.implementation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.request.LoginDTO;
import ru.ntwz.makemyfeed.dto.request.SignUpDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;
import ru.ntwz.makemyfeed.exception.InvalidPasswordException;
import ru.ntwz.makemyfeed.exception.NotAuthorizedException;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.AuthorizationService;
import ru.ntwz.makemyfeed.service.BCryptService;
import ru.ntwz.makemyfeed.service.JWTService;
import ru.ntwz.makemyfeed.service.UserService;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserService userService;

    private final CommonConfig commonConfig;

    private final BCryptService bCryptService;

    private final JWTService jwtService;

    @Autowired
    public AuthorizationServiceImpl(UserService userService, CommonConfig commonConfig, BCryptService bCryptService, JWTService jwtService) {
        this.userService = userService;
        this.commonConfig = commonConfig;
        this.bCryptService = bCryptService;
        this.jwtService = jwtService;
    }

    @Override
    public AccessTokenDTO signUp(@Valid SignUpDTO signUpDTO) {
        String displayName = signUpDTO.getDisplayName();
        String username = signUpDTO.getUsername();
        String password = signUpDTO.getPassword();

        String passwordHash = bCryptService.getHash(password);

        if (displayName == null || displayName.isBlank()) {
            displayName = username;
        }

        User user = new User(displayName, username, passwordHash);

        userService.create(user);

        return new AccessTokenDTO(jwtService.generate(user.getId(), passwordHash));
    }

    @Override
    public AccessTokenDTO login(LoginDTO loginDTO) throws InvalidPasswordException {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = userService.getByUsername(username);

        if (bCryptService.verify(password, user.getPassword()))
            return new AccessTokenDTO(jwtService.generate(user.getId(), user.getPassword()));
        else
            throw new InvalidPasswordException("Invalid password for user: " + username);
    }

    @Override
    public User authUser(String accessToken) throws NotAuthorizedException {
        Long userId = jwtService.validate(accessToken);

        if (userId == null) {
            throw new NotAuthorizedException("Invalid access token");
        }

        try {
            return userService.getById(userId);
        } catch (UserNotFoundException ex) {
            throw new NotAuthorizedException("User not found for access token: " + accessToken);
        }
    }
}
