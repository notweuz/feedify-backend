package ru.ntwz.feedify.service.implementation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.dto.mapper.FollowingMapper;
import ru.ntwz.feedify.dto.mapper.UserMapper;
import ru.ntwz.feedify.dto.response.FollowingDTO;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.exception.AlreadyFollowingException;
import ru.ntwz.feedify.exception.NotFollowingException;
import ru.ntwz.feedify.exception.SelfFollowingException;
import ru.ntwz.feedify.exception.UserNotFoundException;
import ru.ntwz.feedify.model.Following;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.FollowingRepository;
import ru.ntwz.feedify.service.FollowingService;
import ru.ntwz.feedify.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FollowingServiceImpl implements FollowingService {

    private final FollowingRepository followingRepository;
    private final UserService userService;

    @Autowired
    public FollowingServiceImpl(FollowingRepository followingRepository, UserService userService) {
        this.followingRepository = followingRepository;
        this.userService = userService;
    }

    @Override
    public FollowingDTO follow(User follower, String followingUsername) {
        User following = userService.getByUsername(followingUsername);

        if (follower.getId().equals(following.getId())) {
            throw new SelfFollowingException("User cannot follow themselves");
        }

        if (followingRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new AlreadyFollowingException("Already following to user: " + followingUsername);
        }

        Following follow = new Following(follower, following);

        log.info("User {} started following {}", follower.getUsername(), following.getUsername());

        return FollowingMapper.toDTO(followingRepository.save(follow));
    }

    @Override
    @Transactional
    public void unfollow(User follower, String followingUsername) {
        User following = userService.getByUsername(followingUsername);

        if (!followingRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new NotFollowingException("Not followed to user: " + followingUsername);
        }

        log.info("User {} stopped following {}", follower.getUsername(), following.getUsername());

        followingRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<UserDTO> getFollowers(String username, int page, int size) {
        User user = userService.getByUsername(username);

        log.info("Retrieved followers for user: {}", user.getUsername());

        return followingRepository.findByFollowing(user, PageRequest.of(page, size))
                .stream()
                .map(follow -> {
                    User follower = follow.getFollower();
                    return UserMapper.toDTO(follower);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getFollowing(String username, int page, int size) {
        User user = userService.getByUsername(username);

        log.info("Retrieved following for user: {}", user.getUsername());

        return followingRepository.findByFollower(user, PageRequest.of(page, size))
                .stream()
                .map(follow -> {
                    User following = follow.getFollowing();
                    return UserMapper.toDTO(following);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(User follower, String followingUsername) {
        try {
            User following = userService.getByUsername(followingUsername);

            log.info("Checking if user {} is following user {}", follower.getUsername(), following.getUsername());

            return followingRepository.existsByFollowerAndFollowing(follower, following);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
} 