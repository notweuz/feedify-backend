package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.FollowingDto;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.model.User;

import java.util.List;

public interface FollowingService {
    FollowingDto follow(User follower, String followingUsername);

    void unfollow(User follower, String followingUsername);

    List<UserDto> getFollowers(String username, int page, int size);

    List<UserDto> getFollowing(String username, int page, int size);

    boolean isFollowing(User follower, String followingUsername);
}