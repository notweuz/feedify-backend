package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.FollowingDTO;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.model.User;

import java.util.List;

public interface FollowingService {
    FollowingDTO follow(User follower, String followingUsername);

    void unfollow(User follower, String followingUsername);

    List<UserDTO> getFollowers(String username, int page, int size);

    List<UserDTO> getFollowing(String username, int page, int size);

    boolean isFollowing(User follower, String followingUsername);
}