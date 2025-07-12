package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingDto {
    private Long id;
    private UserShortWithFollowersDto follower;
    private UserShortWithFollowersDto following;
    private Instant createdAt;
}