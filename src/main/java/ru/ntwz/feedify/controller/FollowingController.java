package ru.ntwz.feedify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.feedify.dto.response.FollowingDto;
import ru.ntwz.feedify.dto.response.UserShortWithFollowersDto;
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
    public FollowingDto subscribe(
            @PathVariable String username
    ) {
        return followingService.follow(username);
    }

    @DeleteMapping("/{username}")
    public void unsubscribe(
            @PathVariable String username
    ) {
        followingService.unfollow(username);
    }

    @GetMapping("/followers/{username}")
    public List<UserShortWithFollowersDto> getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return followingService.getFollowers(username, page, size);
    }

    @GetMapping("/following/{username}")
    public List<UserShortWithFollowersDto> getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return followingService.getFollowing(username, page, size);
    }

    @GetMapping("/check/{username}")
    public boolean isFollowing(
            @PathVariable String username
    ) {
        return followingService.isFollowing(username);
    }
}