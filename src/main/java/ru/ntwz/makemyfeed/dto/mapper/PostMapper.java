package ru.ntwz.makemyfeed.dto.mapper;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.VoteType;

import java.time.Instant;
import java.util.List;

@Component
public class PostMapper {

    private static CommonConfig commonConfig;

    @Autowired
    public void setCommonConfig(CommonConfig commonConfig) {
        PostMapper.commonConfig = commonConfig;
    }

    private static CommentDTO toCommentDTO(Post post, int currentDepth, int maxDepth) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(post.getId());
        commentDTO.setRating(post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.UPVOTE)).count() -
                post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.DOWNVOTE)).count());
        commentDTO.setContent(post.getContent());
        commentDTO.setAuthor(UserMapper.toDTO(post.getAuthor()));
        commentDTO.setCreatedAt(post.getCreatedAt());
        commentDTO.setCommentsCount(post.getComments().size());
        commentDTO.setIsDeleted(post.getIsDeleted());
        commentDTO.setUniqueLink(post.getUniqueLink());
        commentDTO.setAttachments(post.getAttachments().stream()
                .map(attachment -> StorageMapper.toPostAttachmentDTO(attachment, commonConfig.getPublicDomain() + "/storage/" + attachment.getUniqueName()))
                .toList());

        if (currentDepth < maxDepth) {
            commentDTO.setComments(post.getComments().stream()
                    .map(childPost -> toCommentDTO(childPost, currentDepth + 1, maxDepth))
                    .toList());
        } else {
            commentDTO.setComments(List.of());
        }

        return commentDTO;
    }

    public static CommentDTO toCommentDTO(Post post) {
        return toCommentDTO(post, 0, 1);
    }

    public static PostDTO toPostDTO(@NotNull Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setRating(post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.UPVOTE)).count() -
                post.getVotes().stream().filter(v -> v.getVoteType().equals(VoteType.DOWNVOTE)).count());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(UserMapper.toDTO(post.getAuthor()));
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setComments(post.getComments().stream()
                .map(PostMapper::toCommentDTO)
                .toList());
        postDTO.setCommentsCount(post.getComments().size());
        postDTO.setIsDeleted(post.getIsDeleted());
        postDTO.setUniqueLink(post.getUniqueLink());
        postDTO.setAttachments(post.getAttachments().stream()
                .map(attachment -> StorageMapper.toPostAttachmentDTO(attachment, commonConfig.getPublicDomain() + "/storage/" + attachment.getUniqueName()))
                .toList());

        if (post.getParentPost() != null) {
            PostDTO parentPost = toPostDTO(post.getParentPost());
            parentPost.setParentPost(null);
            parentPost.setComments(List.of());
            postDTO.setParentPost(parentPost);
        }

        return postDTO;
    }

    public static Post toPost(@NotNull PostCreateDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(Instant.now());

        return post;
    }
}
