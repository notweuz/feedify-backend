package ru.ntwz.feedify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.feedify.dto.request.ChangePasswordDto;
import ru.ntwz.feedify.dto.request.UserUpdateDto;
import ru.ntwz.feedify.dto.response.AccessTokenDto;
import ru.ntwz.feedify.dto.response.PostDto;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.service.PostService;
import ru.ntwz.feedify.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public UserController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{userId}/posts")
    public List<PostDto> getPostsByUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return postService.getPostsByUser(userId, page, size);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(
            @PathVariable long userId
    ) {
        return userService.getUserById(userId);
    }

    @GetMapping("/me")
    public UserDto getSelfInfo() {
        return userService.getUserInfo();
    }

    @GetMapping("/usernames/{username}")
    public UserDto getPostsByUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PatchMapping("/me")
    public UserDto update(
            @RequestBody @Valid UserUpdateDto userUpdateDTO
    ) {
        return userService.updateUserDto(userUpdateDTO);
    }

    @PostMapping("/me/password")
    public AccessTokenDto changePassword(
            @RequestBody @Valid ChangePasswordDto changePasswordDTO
    ) {
        return userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
    }
}
