package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.request.PostCreateDto;
import ru.ntwz.feedify.dto.request.PostUpdateDto;
import ru.ntwz.feedify.dto.response.CommentDto;
import ru.ntwz.feedify.dto.response.PostDTO;
import ru.ntwz.feedify.model.User;

import java.util.List;

public interface PostService {
    PostDTO create(User user, PostCreateDto post);

    PostDTO findById(Long id);

    List<PostDTO> getPostsByUser(long userId, int page, int size);

    PostDTO createComment(User user, PostCreateDto post, Long parentPostId);

    PostDTO findByUniqueLink(String uniqueLink);

    List<CommentDto> getComments(Long parentPostId, int page, int size);

    PostDTO update(User user, Long id, PostUpdateDto postUpdateDTO);

    void delete(User user, Long id);

    void deleteAttachment(User user, Long postId, Long attachmentId);

    List<PostDTO> findUserRecommendations(User user, int page, int size);

    List<PostDTO> findAllRecentPosts(int page, int size);

    List<PostDTO> findAllMonthlyPopularPosts(int page, int size);
}
