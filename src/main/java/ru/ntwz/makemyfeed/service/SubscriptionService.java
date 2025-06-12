package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.response.SubscriptionDTO;
import ru.ntwz.makemyfeed.model.User;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDTO subscribe(User follower, String followingUsername);
    
    void unsubscribe(User follower, String followingUsername);
    
    List<SubscriptionDTO> getFollowers(String username, int page, int size);
    
    List<SubscriptionDTO> getFollowing(String username, int page, int size);
    
    boolean isFollowing(User follower, String followingUsername);
} 