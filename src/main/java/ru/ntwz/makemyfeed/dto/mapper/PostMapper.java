package ru.ntwz.makemyfeed.dto.mapper;

import jakarta.validation.constraints.NotNull;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.Post;

import java.time.Instant;

public class PostMapper {
    public static CommentDTO toCommentDTO(Post post) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(post.getId());
        commentDTO.setRating(post.getRating());
        commentDTO.setContent(post.getContent());
        commentDTO.setAuthor(UserMapper.toDTO(post.getAuthor()));
        commentDTO.setCreatedAt(post.getCreatedAt());
        commentDTO.setCommentsCount(post.getComments().size());
        commentDTO.setIsDeleted(post.getIsDeleted());

        return commentDTO;
    }

    public static PostDTO toPostDTO(@NotNull Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setRating(post.getRating());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(UserMapper.toDTO(post.getAuthor()));
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setComments(post.getComments().stream()
                .map(PostMapper::toCommentDTO)
                .toList());
        postDTO.setCommentsCount(post.getComments().size());
        postDTO.setIsDeleted(post.getIsDeleted());

        return postDTO;
    }

    public static Post toPost(@NotNull PostCreateDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(Instant.now());
        post.setRating(0);

        return post;
    }
}
