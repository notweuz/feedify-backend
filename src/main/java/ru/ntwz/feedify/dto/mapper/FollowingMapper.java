package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.FollowingDTO;
import ru.ntwz.feedify.model.Following;

public class FollowingMapper {
    public static FollowingDTO toDTO(Following subscription) {
        return new FollowingDTO(
                subscription.getId(),
                UserMapper.toDTO(subscription.getFollower()),
                UserMapper.toDTO(subscription.getFollowing()),
                subscription.getCreatedAt()
        );
    }
} 