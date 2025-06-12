package ru.ntwz.makemyfeed.service.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.FollowingsMapper;
import ru.ntwz.makemyfeed.dto.mapper.UserMapper;
import ru.ntwz.makemyfeed.dto.response.FollowingDTO;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.exception.AlreadyFollowingException;
import ru.ntwz.makemyfeed.exception.NotFollowingException;
import ru.ntwz.makemyfeed.exception.SelfFollowingException;
import ru.ntwz.makemyfeed.exception.UserNotFoundException;
import ru.ntwz.makemyfeed.model.Following;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.FollowingRepository;
import ru.ntwz.makemyfeed.service.FollowingService;
import ru.ntwz.makemyfeed.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        return FollowingsMapper.toDTO(followingRepository.save(follow));
    }

    @Override
    @Transactional
    public void unfollow(User follower, String followingUsername) {
        User following = userService.getByUsername(followingUsername);

        if (!followingRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new NotFollowingException("Not followed to user: " + followingUsername);
        }

        followingRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<UserDTO> getFollowers(String username, int page, int size) {
        User user = userService.getByUsername(username);

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
            return followingRepository.existsByFollowerAndFollowing(follower, following);
        } catch (UserNotFoundException e) {
            return false;
        }
    }
} 