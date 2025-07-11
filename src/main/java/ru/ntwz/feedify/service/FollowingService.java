package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.FollowingDto;
import ru.ntwz.feedify.dto.response.UserDto;

import java.util.List;

public interface FollowingService {
    FollowingDto follow(String followingUsername);

    void unfollow(String followingUsername);

    List<UserDto> getFollowers(String username, int page, int size);

    List<UserDto> getFollowing(String username, int page, int size);

    boolean isFollowing(String followingUsername);
}