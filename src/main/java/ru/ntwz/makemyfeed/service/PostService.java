package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.User;

public interface PostService {
    PostDTO create(User user, PostCreateDTO post);
}
