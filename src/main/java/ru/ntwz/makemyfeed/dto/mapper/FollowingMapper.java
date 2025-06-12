package ru.ntwz.makemyfeed.dto.mapper;

import ru.ntwz.makemyfeed.dto.response.FollowingDTO;
import ru.ntwz.makemyfeed.model.Following;

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