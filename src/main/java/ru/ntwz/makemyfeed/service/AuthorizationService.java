package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.request.SignUpDTO;
import ru.ntwz.makemyfeed.dto.response.AccessTokenDTO;

public interface AuthorizationService {
    AccessTokenDTO signUp(SignUpDTO signUpDTO);
}
