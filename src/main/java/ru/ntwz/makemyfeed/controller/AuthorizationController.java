package ru.ntwz.makemyfeed.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ntwz.makemyfeed.dto.request.SignUpDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;
import ru.ntwz.makemyfeed.service.AuthorizationService;

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
}
