package ru.ntwz.feedify.dto.mapper;

import jakarta.validation.constraints.NotNull;
import ru.ntwz.feedify.dto.request.PostCreateDto;
import ru.ntwz.feedify.dto.response.PostDto;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.VoteType;

import java.time.Instant;

public class PostMapper {
    private static PostDto mapPostToDTO(Post post, boolean includeParent) {
        PostDto postDTO = new PostDto();

        postDTO.setId(post.getId());
        postDTO.setRating(post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.UPVOTE)).count() -
                post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.DOWNVOTE)).count());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(UserMapper.toUserShortDto(post.getAuthor()));
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setCommentsCount(post.getComments().size());
        postDTO.setIsDeleted(post.getIsDeleted());
        postDTO.setUniqueLink(post.getUniqueLink());
        postDTO.setAttachments(post.getAttachments().stream()
                .map(attachment -> StorageMapper.toPostAttachmentDTO(attachment, StorageMapper.getStorageUrl(attachment.getUniqueName())))
                .toList());

        if (includeParent && post.getParentPost() != null) {
            postDTO.setParentPost(mapPostToDTO(post.getParentPost(), false));
        }

        return postDTO;
    }

    public static PostDto toPostDTO(@NotNull Post post) {
        return mapPostToDTO(post, true);
    }

    public static Post toPost(@NotNull PostCreateDto postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(Instant.now());

        return post;
    }
}
