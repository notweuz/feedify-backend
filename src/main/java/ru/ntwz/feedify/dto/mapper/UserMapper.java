package ru.ntwz.feedify.dto.mapper;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.response.UserDTO;
import ru.ntwz.feedify.model.User;

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
        userDTO.setDescription(user.getDescription() != null ? user.getDescription() : "");
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setPostsCount(user.getPosts().size());
        userDTO.setFollowersCount(user.getFollowers().size());
        userDTO.setFollowingCount(user.getFollowing().size());
        userDTO.setAvatarUrl(user.getAvatar() != null ? commonConfig.getPublicDomain() + "/storage/" + user.getAvatar().getUniqueName() : null);
        userDTO.setBannerUrl(user.getBanner() != null ? commonConfig.getPublicDomain() + "/storage/" + user.getBanner().getUniqueName() : null);
        return userDTO;
    }
}
