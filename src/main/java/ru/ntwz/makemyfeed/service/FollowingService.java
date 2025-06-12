package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.response.FollowingDTO;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.model.User;

import java.util.List;

public interface FollowingService {
    FollowingDTO follow(User follower, String followingUsername);
    
    void unfollow(User follower, String followingUsername);
    
    List<UserDTO> getFollowers(String username, int page, int size);
    
    List<UserDTO> getFollowing(String username, int page, int size);
    
    boolean isFollowing(User follower, String followingUsername);
} 