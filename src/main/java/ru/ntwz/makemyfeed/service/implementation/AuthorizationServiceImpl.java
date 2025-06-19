package ru.ntwz.makemyfeed.service.implementation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.request.LoginDTO;
import ru.ntwz.makemyfeed.dto.request.SignUpDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenStatusDTO;
import ru.ntwz.makemyfeed.exception.InvalidPasswordException;
import ru.ntwz.makemyfeed.exception.NotAuthorizedException;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.AuthorizationService;
import ru.ntwz.makemyfeed.service.BCryptService;
import ru.ntwz.makemyfeed.service.JWTService;
import ru.ntwz.makemyfeed.service.UserService;

@Service
@Slf4j
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

        log.info("User {} created successfully", username);

        String token = jwtService.generate(user.getId(), passwordHash);
        log.debug("Generated token for user {}: {}", username, token.substring(0, Math.min(20, token.length())) + "...");
        
        return new AccessTokenDTO(token);
    }

    @Override
    public AccessTokenDTO login(LoginDTO loginDTO) throws InvalidPasswordException {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = userService.getByUsername(username);

        log.info("User {} attempting to log in", username);

        if (bCryptService.verify(password, user.getPassword())) {
            String token = jwtService.generate(user.getId(), user.getPassword());
            log.debug("Generated token for user {}: {}", username, token.substring(0, Math.min(20, token.length())) + "...");
            return new AccessTokenDTO(token);
        } else {
            throw new InvalidPasswordException("Invalid password for user: " + username);
        }
    }

    @Override
    public AccessTokenStatusDTO validate(AccessTokenDTO accessTokenDTO) {
        String accessToken = accessTokenDTO.getAccessToken();
        
        if (accessToken == null || accessToken.isBlank()) {
            log.info("Token is null or blank");
            return new AccessTokenStatusDTO(false);
        }
        
        log.info("Validating token: {}", accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
        
        try {
            Long userId = jwtService.validate(accessToken);
            log.info("Token validation successful, userId: {}", userId);
            return new AccessTokenStatusDTO(userId != null);
        } catch (NotAuthorizedException e) {
            log.info("Token validation failed - NotAuthorizedException: {}", e.getMessage());
            return new AccessTokenStatusDTO(false);
        } catch (Exception e) {
            log.info("Token validation failed - unexpected exception: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return new AccessTokenStatusDTO(false);
        }
    }

    @Override
    public User authUser(String accessToken) throws NotAuthorizedException {
        Long userId = jwtService.validate(accessToken);

        if (userId == null) {
            throw new NotAuthorizedException("Invalid access token");
        }

        try {
            User user = userService.getById(userId);
            String tokenPasswordHash = jwtService.extractPasswordHash(accessToken);
            if (!user.getPassword().equals(tokenPasswordHash)) {
                throw new NotAuthorizedException("Token is outdated due to password change");
            }
            return user;
        } catch (UserNotFoundException ex) {
            throw new NotAuthorizedException("User not found for access token: " + accessToken);
        }
    }
}
