package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.LoginDto;
import ru.ntwz.feedify.dto.request.SignUpDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.AccessTokenStatusDto;

public interface AuthorizationService {
    AccessTokenDto signUp(SignUpDto signUpDTO);

    AccessTokenDto login(LoginDto loginDTO);

    AccessTokenStatusDto validate(AccessTokenDto accessTokenDTO);
}
