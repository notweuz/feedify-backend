package ru.ntwz.makemyfeed.service.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.SubscriptionMapper;
import ru.ntwz.makemyfeed.dto.mapper.UserMapper;
import ru.ntwz.makemyfeed.dto.response.SubscriptionDTO;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.exception.AlreadySubscribedException;
import ru.ntwz.makemyfeed.exception.NotSubscribedException;
import ru.ntwz.makemyfeed.exception.SelfSubscriptionException;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.Subscription;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.SubscriptionRepository;
import ru.ntwz.makemyfeed.service.SubscriptionService;
import ru.ntwz.makemyfeed.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
    }

    @Override
    public SubscriptionDTO subscribe(User follower, String followingUsername) {
        User following = userService.getByUsername(followingUsername);

        if (follower.getId().equals(following.getId())) {
            throw new SelfSubscriptionException("User cannot subscribe to themselves");
        }

        if (subscriptionRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new AlreadySubscribedException("Already subscribed to user: " + followingUsername);
        }

        Subscription subscription = new Subscription(follower, following);
        return SubscriptionMapper.toDTO(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional
    public void unsubscribe(User follower, String followingUsername) {
        User following = userService.getByUsername(followingUsername);

        if (!subscriptionRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new NotSubscribedException("Not subscribed to user: " + followingUsername);
        }

        subscriptionRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<UserDTO> getFollowers(String username, int page, int size) {
        User user = userService.getByUsername(username);

        return subscriptionRepository.findByFollowing(user, PageRequest.of(page, size))
                .stream()
                .map(subscription -> {
                    User follower = subscription.getFollower();
                    return UserMapper.toDTO(follower);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getFollowing(String username, int page, int size) {
        User user = userService.getByUsername(username);

        return subscriptionRepository.findByFollower(user, PageRequest.of(page, size))
                .stream()
                .map(subscription -> {
                    User following = subscription.getFollowing();
                    return UserMapper.toDTO(following);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(User follower, String followingUsername) {
        try {
            User following = userService.getByUsername(followingUsername);
            return subscriptionRepository.existsByFollowerAndFollowing(follower, following);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
} 