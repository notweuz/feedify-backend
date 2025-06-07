package ru.ntwz.makemyfeed.service.implementation;

import jakarta.transaction.Transactional;
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

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(@Autowired PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDTO create(User user, PostCreateDTO postCreateDTO) {
        String content = postCreateDTO.getContent();
        if (postCreateDTO.getTitle() == null || postCreateDTO.getTitle().isEmpty()) {
            String title = content != null && content.length() > 20 ? content.substring(0, 20) : content;
            postCreateDTO.setTitle(title);
        }

        Post post = PostMapper.toPost(postCreateDTO);
        post.setAuthor(user);

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        return PostMapper.toPostDTO(post);
    }
}
