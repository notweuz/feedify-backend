package ru.ntwz.makemyfeed.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.PostMapper;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.exception.NotPostsOwnerException;
import ru.ntwz.makemyfeed.exception.PostAlreadyDeletedException;
import ru.ntwz.makemyfeed.exception.PostNotFoundException;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.PostRepository;
import ru.ntwz.makemyfeed.service.PostService;
import ru.ntwz.makemyfeed.service.UserService;

import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImpl(@Autowired PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public PostDTO create(User user, PostCreateDTO postCreateDTO) {
        Post post = PostMapper.toPost(postCreateDTO);
        post.setAuthor(user);

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        List<Post> comments = postRepository.findTop10CommentsByParentPostId(id, Pageable.ofSize(4)).getContent();

        PostDTO postDTO = PostMapper.toPostDTO(post);
        postDTO.setComments(comments.stream()
                .map(PostMapper::toCommentDTO)
                .toList());

        return postDTO;
    }

    @Override
    public List<PostDTO> getPostsByUser(long userId, int page, int size) {
        User user = userService.getById(userId);

        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByAuthor(user, pageable).getContent().stream().map(PostMapper::toPostDTO).toList();
    }

    @Override
    public PostDTO createComment(User user, PostCreateDTO createDTO, Long parentPostId) {
        Post parentPost = postRepository.findById(parentPostId).orElseThrow(() -> new PostNotFoundException("Post with id " + parentPostId + " not found"));

        Post post = PostMapper.toPost(createDTO);
        post.setParentPost(parentPost);
        post.setAuthor(user);

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public PostDTO findByUniqueLink(String uniqueLink) {
        Post post = postRepository.findByUniqueLink(uniqueLink)
                .orElseThrow(() -> new PostNotFoundException("Post with unique link '" + uniqueLink + "' not found"));

        return PostMapper.toPostDTO(post);
    }

    @Override
    public List<CommentDTO> getComments(Long parentPostId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> comments = postRepository.findTopCommentsByParentPostId(parentPostId, pageable).getContent();
        return comments.stream()
                .map(PostMapper::toCommentDTO)
                .toList();
    }

    @Override
    public PostDTO update(User user, Long id, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        if (!post.getAuthor().equals(user)) throw new NotPostsOwnerException("You are not the owner of this post");

        if (postUpdateDTO.getContent() != null && !postUpdateDTO.getContent().isBlank()) {
            post.setContent(postUpdateDTO.getContent());
        }

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public void delete(User user, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        if (!post.getAuthor().equals(user)) throw new NotPostsOwnerException("You are not the owner of this post");
        if (post.getIsDeleted()) throw new PostAlreadyDeletedException("Post with id " + id + " is already deleted");

        post.setIsDeleted(true);
        post.setContent(null);

        postRepository.save(post);
    }
}
