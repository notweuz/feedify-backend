package ru.ntwz.feedify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.feedify.constant.AttributesConstants;
import ru.ntwz.feedify.dto.response.FollowingDTO;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.service.FollowingService;

import java.util.List;

@RestController
@RequestMapping("/followings/")
public class FollowingController {

    private final FollowingService followingService;

    @Autowired
    public FollowingController(FollowingService followingService) {
        this.followingService = followingService;
    }

    @PostMapping("/{username}")
    public FollowingDTO subscribe(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        return followingService.follow(user, username);
    }

    @DeleteMapping("/{username}")
    public void unsubscribe(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        followingService.unfollow(user, username);
    }

    @GetMapping("/followers/{username}")
    public List<UserDTO> getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return followingService.getFollowers(username, page, size);
    }

    @GetMapping("/following/{username}")
    public List<UserDTO> getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return followingService.getFollowing(username, page, size);
    }

    @GetMapping("/check/{username}")
    public boolean isFollowing(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        return followingService.isFollowing(user, username);
    }
}