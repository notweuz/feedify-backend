package ru.ntwz.feedify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ntwz.feedify.dto.request.LoginDTO;
import ru.ntwz.feedify.dto.request.SignUpDTO;
import ru.ntwz.feedify.dto.response.AccessTokenDTO;
import ru.ntwz.feedify.dto.response.AccessTokenStatusDTO;
import ru.ntwz.feedify.service.AuthorizationService;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(@Autowired AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public AccessTokenDTO signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
        return authorizationService.signUp(signUpDTO);
    }

    @PostMapping("/login")
    public AccessTokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return authorizationService.login(loginDTO);
    }

    @PostMapping("/validate")
    public AccessTokenStatusDTO validate(@RequestBody @Valid AccessTokenDTO accessTokenDTO) {
        return authorizationService.validate(accessTokenDTO);
    }
}
