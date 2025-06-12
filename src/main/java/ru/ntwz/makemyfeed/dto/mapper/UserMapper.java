package ru.ntwz.makemyfeed.dto.mapper;

import jakarta.validation.constraints.NotNull;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.model.User;

public class UserMapper {
    public static UserDTO toDTO(@NotNull User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setUsername(user.getUsername());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setFollowersCount(user.getFollowers().size());
        userDTO.setFollowingCount(user.getFollowing().size());
        return userDTO;
    }

    public static User toUser(@NotNull UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setDisplayName(userDTO.getDisplayName());
        user.setUsername(userDTO.getUsername());
        user.setRegistrationDate(userDTO.getRegistrationDate());
        return user;
    }
}
