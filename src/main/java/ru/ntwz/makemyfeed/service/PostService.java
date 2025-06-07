package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.User;

public interface PostService {
    PostDTO create(User user, PostCreateDTO post);

    PostDTO findById(Long id);

    PostDTO createComment(User user, PostCreateDTO post, Long parentPostId);

    PostDTO update(User user, Long id, PostUpdateDTO postUpdateDTO);

    void delete(User user, Long id);
}
