package ru.ntwz.makemyfeed.dto.mapper;

import ru.ntwz.makemyfeed.dto.response.SubscriptionDTO;
import ru.ntwz.makemyfeed.model.Subscription;

public class SubscriptionMapper {
    public static SubscriptionDTO toDTO(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                UserMapper.toDTO(subscription.getFollower()),
                UserMapper.toDTO(subscription.getFollowing()),
                subscription.getCreatedAt()
        );
    }
} 