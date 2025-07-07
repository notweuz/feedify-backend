package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.VoteDto;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.Vote;
import ru.ntwz.feedify.model.VoteType;

public class VoteMapper {
    public static VoteDto toVoteDTO(Post post, Vote vote) {
        VoteDto voteDTO = new VoteDto();
        voteDTO.setPostId(post.getId());
        voteDTO.setRating(post.getVotes().stream()
                .filter(v -> v.getVoteType().equals(VoteType.UPVOTE)).count() -
                post.getVotes().stream()
                        .filter(v -> v.getVoteType().equals(VoteType.DOWNVOTE)).count());
        voteDTO.setVoteType(vote != null ? vote.getVoteType() : VoteType.UNVOTED);
        return voteDTO;
    }
}
