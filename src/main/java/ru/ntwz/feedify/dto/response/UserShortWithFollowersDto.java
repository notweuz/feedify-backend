package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortWithFollowersDto {
    private Long id;
    private String displayName;
    private String username;
    private String avatarUrl;
    private Integer followersCount;
}
