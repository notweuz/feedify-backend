package ru.ntwz.makemyfeed.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.User;

import java.util.List;

public interface PostService {
    PostDTO create(User user, PostCreateDTO post);

    PostDTO findById(Long id);

    List<PostDTO> getPostsByUser(long userId, int page, int size);

    PostDTO createComment(User user, PostCreateDTO post, Long parentPostId);

    PostDTO findByUniqueLink(String uniqueLink);

    List<CommentDTO> getComments(Long parentPostId, int page, int size);

    PostDTO update(User user, Long id, PostUpdateDTO postUpdateDTO);

    void delete(User user, Long id);

    void deleteAttachment(User user, Long postId, Long attachmentId);

    List<PostDTO> findUserRecommendations(User user, int page, int size);

    List<PostDTO> findAllRecentPosts(int page, int size);

    List<PostDTO> findAllMonthlyPopularPosts(int page, int size);
}
