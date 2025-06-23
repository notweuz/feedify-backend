package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.LoginDTO;
import ru.ntwz.feedify.dto.request.SignUpDTO;
import ru.ntwz.feedify.dto.response.AccessTokenDTO;
import ru.ntwz.feedify.dto.response.AccessTokenStatusDTO;
import ru.ntwz.feedify.exception.NotAuthorizedException;
import ru.ntwz.feedify.model.User;

public interface AuthorizationService {
    AccessTokenDTO signUp(SignUpDTO signUpDTO);

    AccessTokenDTO login(LoginDTO loginDTO);

    AccessTokenStatusDTO validate(AccessTokenDTO accessTokenDTO);

    User authUser(String accessToken) throws NotAuthorizedException;
}
