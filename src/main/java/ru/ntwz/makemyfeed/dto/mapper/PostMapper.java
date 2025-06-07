package ru.ntwz.makemyfeed.dto.mapper;

import jakarta.validation.constraints.NotNull;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.Post;

import java.time.Instant;

public class PostMapper {
    public static PostDTO toPostDTO(@NotNull Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setRating(post.getRating());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(UserMapper.toDTO(post.getAuthor()));
        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }

    public static Post toPost(@NotNull PostCreateDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreatedAt(Instant.now());

        return post;
    }
}
