package ru.ntwz.makemyfeed.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.PostService;
import ru.ntwz.makemyfeed.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final VoteService voteService;

    @Autowired
    public PostController(PostService postService, VoteService voteService) {
        this.postService = postService;
        this.voteService = voteService;
    }

    @PostMapping
    public PostDTO create(
            @RequestAttribute(AttributesConstants.USER) User user,
            @RequestBody @Valid PostCreateDTO postCreateDTO
    ) {
        return postService.create(user, postCreateDTO);
    }

    @PostMapping("/{id}/comments")
    public PostDTO createComment(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable Long id,
            @RequestBody @Valid PostCreateDTO postCreateDTO
    ) {
        return postService.createComment(user, postCreateDTO, id);
    }

    @GetMapping("/{id}/comments")
    public List<CommentDTO> getComments(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return postService.getComments(id, page, size);
    }

    @GetMapping("/{id}")
    public PostDTO getById(@PathVariable long id) {
        return postService.findById(id);
    }

    @PatchMapping("/{id}")
    public PostDTO update(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable long id,
            @RequestBody @Valid PostUpdateDTO postUpdateDTO
    ) {
        return postService.update(user, id, postUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable long id
    ) {
        postService.delete(user, id);
    }

    @PostMapping("/{postId}/vote")
    public void upvote(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "true") boolean upvote
    ) {
        voteService.vote(postId, user, upvote);
    }
}
