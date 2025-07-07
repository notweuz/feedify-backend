package ru.ntwz.feedify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ntwz.feedify.dto.request.LoginDto;
import ru.ntwz.feedify.dto.request.SignUpDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.AccessTokenStatusDto;
import ru.ntwz.feedify.service.AuthorizationService;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(@Autowired AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public AccessTokenDto signUp(@RequestBody @Valid SignUpDto signUpDTO) {
        return authorizationService.signUp(signUpDTO);
    }

    @PostMapping("/login")
    public AccessTokenDto login(@RequestBody @Valid LoginDto loginDTO) {
        return authorizationService.login(loginDTO);
    }

    @PostMapping("/validate")
    public AccessTokenStatusDto validate(@RequestBody @Valid AccessTokenDto accessTokenDTO) {
        return authorizationService.validate(accessTokenDTO);
    }
}
