package ru.ntwz.feedify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.feedify.constant.AttributesConstants;
import ru.ntwz.feedify.dto.request.ChangePasswordDTO;
import ru.ntwz.feedify.dto.request.UserUpdateDTO;
import ru.ntwz.feedify.dto.response.AccessTokenDTO;
import ru.ntwz.feedify.dto.response.PostDTO;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.model.User;
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
    public List<PostDTO> getPostsByUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return postService.getPostsByUser(userId, page, size);
    }

    @GetMapping("/me")
    public UserDTO getSelfInfo(
            @RequestAttribute(AttributesConstants.USER) User user
    ) {
        return userService.getUserInfo(user);
    }

    @GetMapping("/usernames/{username}")
    public UserDTO getPostsByUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PatchMapping("/me")
    public UserDTO update(
            @RequestAttribute(AttributesConstants.USER) User user,
            @RequestBody @Valid UserUpdateDTO userUpdateDTO
    ) {
        return userService.updateUser(user, userUpdateDTO);
    }

    @PostMapping("/me/change-password")
    public AccessTokenDTO changePassword(
            @RequestAttribute(AttributesConstants.USER) User user,
            @RequestBody @Valid ChangePasswordDTO changePasswordDTO
    ) {
        return userService.changePassword(user, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
    }
}
