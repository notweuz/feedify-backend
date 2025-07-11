package ru.ntwz.feedify.service.implementation;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.request.LoginDto;
import ru.ntwz.feedify.dto.request.SignUpDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.AccessTokenStatusDto;
import ru.ntwz.feedify.exception.InvalidPasswordException;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.security.BCryptServicePasswordEncoder;
import ru.ntwz.feedify.service.AuthorizationService;
import ru.ntwz.feedify.service.JWTService;
import ru.ntwz.feedify.service.UserService;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;
    private final CommonConfig commonConfig;
    private final BCryptServicePasswordEncoder bCryptServicePasswordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthorizationServiceImpl(
            UserService userService,
            CommonConfig commonConfig,
            BCryptServicePasswordEncoder bCryptServicePasswordEncoder,
            JWTService jwtService, AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.commonConfig = commonConfig;
        this.bCryptServicePasswordEncoder = bCryptServicePasswordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AccessTokenDto signUp(@Valid SignUpDto signUpDto) {
        log.info("User {} attempting to sign up", signUpDto.getUsername());

        User user = new User();
        user.setDisplayName(signUpDto.getDisplayName());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(bCryptServicePasswordEncoder.encode(signUpDto.getPassword()));

        userService.create(user);
        log.info("User {} created successfully", user.getUsername());

        String jwt = jwtService.generateToken(user);
        log.debug("Generated token for user {} after registration: {}", user.getUsername(), jwt.substring(0, Math.min(20, jwt.length())) + "...");
        return new AccessTokenDto(jwt);
    }

    @Override
    public AccessTokenDto login(@Valid LoginDto loginDto) throws InvalidPasswordException {
        log.info("User {} attempting to log in", loginDto.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
        } catch (BadCredentialsException e) {
            log.warn("Invalid password attempt for user: {}", loginDto.getUsername());
            throw new InvalidPasswordException("Invalid password provided");
        }

        UserDetails user = userService.userDetailsService().loadUserByUsername(loginDto.getUsername());
        String jwt = jwtService.generateToken(user);
        log.debug("Generated token for user {} after login: {}", user.getUsername(),
                jwt.substring(0, Math.min(20, jwt.length())) + "...");
        return new AccessTokenDto(jwt);
    }

    @Override
    public AccessTokenStatusDto validate(@Valid AccessTokenDto accessTokenDto) {
        return null;
    }
}
