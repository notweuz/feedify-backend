package ru.ntwz.makemyfeed.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(@Autowired PostService postService) {
        this.postService = postService;
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
}
