package ru.ntwz.makemyfeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
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
    public PostDTO create(@RequestAttribute(AttributesConstants.USER) User user,
                              @RequestBody PostCreateDTO postCreateDTO) {
        return postService.create(user, postCreateDTO);
    }

    @GetMapping("/{id}")
    public PostDTO getById(@PathVariable long id) {
        return postService.findById(id);
    }

//    @GetMapping
//    public List<PostDTO> getAllPostsAvailableForUser(@RequestAttribute(AttributesConstants.USER) User user,
//                                                     @RequestParam(value = "page", defaultValue = "0") int page,
//                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
//        return postService.getAllPostsAvailableForUser(user, page, size);
//    }
}
