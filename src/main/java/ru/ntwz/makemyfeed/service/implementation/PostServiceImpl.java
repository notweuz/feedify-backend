package ru.ntwz.makemyfeed.service.implementation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.PostMapper;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.exception.PostNotFoundException;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.PostRepository;
import ru.ntwz.makemyfeed.service.PostService;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(@Autowired PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDTO create(User user, PostCreateDTO postCreateDTO) {
        Post post = PostMapper.toPost(postCreateDTO);
        post.setAuthor(user);

        log.info("Post created: {}", post);

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

//        log.info("Post found: {}", post);

        return PostMapper.toPostDTO(post);
    }

    @Override
    @Transactional
    public PostDTO createComment(User user, PostCreateDTO createDTO, Long parentPostId) {
        Post parentPost = postRepository.findById(parentPostId).orElseThrow(() -> new PostNotFoundException("Post with id " + parentPostId + " not found"));

        Post post = PostMapper.toPost(createDTO);
        post.setParentPost(parentPost);
        post.setAuthor(user);

//        log.info("Post comment created: {}", post);

        return PostMapper.toPostDTO(postRepository.save(post));
    }
}
