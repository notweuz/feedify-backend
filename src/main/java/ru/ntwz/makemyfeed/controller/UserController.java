package ru.ntwz.makemyfeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PostService postService;

    @Autowired
    public UserController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{userId}/posts")
    public List<PostDTO> getPostsByUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return postService.getPostsByUser(userId, page, size);
    }
}
