package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.FollowingDto;
import ru.ntwz.feedify.model.Following;

public class FollowingMapper {
    public static FollowingDto toDTO(Following subscription) {
        return new FollowingDto(
                subscription.getId(),
                UserMapper.toUserShortWithFollowersDto(subscription.getFollower()),
                UserMapper.toUserShortWithFollowersDto(subscription.getFollowing()),
                subscription.getCreatedAt()
        );
    }
}