package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserShortDto {
    private Long id;
    private String displayName;
    private String username;
    private String avatarUrl;
}
