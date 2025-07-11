package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.PostCreateDto;
import ru.ntwz.feedify.dto.request.PostUpdateDto;
import ru.ntwz.feedify.dto.response.PostDto;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.User;

import java.util.List;

public interface PostService {
    PostDto create(PostCreateDto post);

    PostDto findById(Long id);

    List<PostDto> getPostsByUser(long userId, int page, int size);

    PostDto createComment(PostCreateDto post, Long parentPostId);

    PostDto findByUniqueLink(String uniqueLink);

    List<PostDto> getComments(Long parentPostId, int page, int size);

    PostDto update(Long id, PostUpdateDto postUpdateDTO);

    void delete(Long id);

    void deleteAttachment(Long postId, Long attachmentId);

    List<PostDto> findUserRecommendations(int page, int size);

    List<PostDto> findAllRecentPosts(int page, int size);

    List<PostDto> findAllMonthlyPopularPosts(int page, int size);

    Post getPostOrThrow(Long postId);
}
