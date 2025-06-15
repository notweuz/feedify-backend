package ru.ntwz.makemyfeed.dto.mapper;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.response.UserDTO;
import ru.ntwz.makemyfeed.model.User;

@Component
public class UserMapper {

    private static CommonConfig commonConfig;

    @Autowired
    public void setCommonConfig(CommonConfig commonConfig) {
        UserMapper.commonConfig = commonConfig;
    }

    public static UserDTO toDTO(@NotNull User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setUsername(user.getUsername());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setFollowersCount(user.getFollowers().size());
        userDTO.setFollowingCount(user.getFollowing().size());
        userDTO.setAvatarUrl(user.getAvatar() != null ? commonConfig.getPublicDomain() + "/storage/" + user.getAvatar().getUniqueName() : null);
        return userDTO;
    }
}
