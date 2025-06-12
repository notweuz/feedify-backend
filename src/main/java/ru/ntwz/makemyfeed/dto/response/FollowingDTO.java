package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingDTO {
    private Long id;
    private UserDTO follower;
    private UserDTO following;
    private Instant createdAt;
} 