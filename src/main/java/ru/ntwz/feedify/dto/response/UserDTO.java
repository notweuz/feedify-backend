package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String displayName;
    private String username;
    private String description;
    private String avatarUrl;
    private String bannerUrl;
    private Instant registrationDate;
    private Integer postsCount;
    private Integer followersCount;
    private Integer followingCount;
}
