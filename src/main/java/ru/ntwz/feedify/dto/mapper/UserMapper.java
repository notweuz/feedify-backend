package ru.ntwz.feedify.dto.mapper;

import jakarta.validation.constraints.NotNull;
import ru.ntwz.feedify.dto.response.UserDto;
import ru.ntwz.feedify.dto.response.UserShortDto;
import ru.ntwz.feedify.dto.response.UserShortWithFollowersDto;
import ru.ntwz.feedify.model.User;

public class UserMapper {
    public static UserDto toDTO(@NotNull User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setUsername(user.getUsername());
        userDTO.setDescription(user.getDescription() != null ? user.getDescription() : "");
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setPostsCount(user.getPosts().size());
        userDTO.setFollowersCount(user.getFollowers().size());
        userDTO.setFollowingCount(user.getFollowing().size());
        userDTO.setAvatarUrl(user.getAvatar() != null ? StorageMapper.getStorageUrl(user.getAvatar().getUniqueName()) : null);
        userDTO.setBannerUrl(user.getBanner() != null ? StorageMapper.getStorageUrl(user.getBanner().getUniqueName()) : null);
        return userDTO;
    }

    public static UserShortDto toUserShortDto(@NotNull User user) {
        UserShortDto userShortDTO = new UserShortDto();
        userShortDTO.setId(user.getId());
        userShortDTO.setDisplayName(user.getDisplayName());
        userShortDTO.setUsername(user.getUsername());
        userShortDTO.setAvatarUrl(user.getAvatar() != null ? StorageMapper.getStorageUrl(user.getAvatar().getUniqueName()) : null);
        return userShortDTO;
    }

    public static UserShortWithFollowersDto toUserShortWithFollowersDto(@NotNull User user) {
        UserShortWithFollowersDto userShortWithFollowersDTO = new UserShortWithFollowersDto();
        userShortWithFollowersDTO.setId(user.getId());
        userShortWithFollowersDTO.setDisplayName(user.getDisplayName());
        userShortWithFollowersDTO.setUsername(user.getUsername());
        userShortWithFollowersDTO.setAvatarUrl(user.getAvatar() != null ? StorageMapper.getStorageUrl(user.getAvatar().getUniqueName()) : null);
        userShortWithFollowersDTO.setFollowersCount(user.getFollowers().size());
        return userShortWithFollowersDTO;
    }
}
