package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.request.LoginDTO;
import ru.ntwz.makemyfeed.dto.request.SignUpDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenStatusDTO;
import ru.ntwz.makemyfeed.exception.NotAuthorizedException;
import ru.ntwz.makemyfeed.model.User;

public interface AuthorizationService {
    AccessTokenDTO signUp(SignUpDTO signUpDTO);

    AccessTokenDTO login(LoginDTO loginDTO);

    AccessTokenStatusDTO validate(AccessTokenDTO accessTokenDTO);

    User authUser(String accessToken) throws NotAuthorizedException;
}
