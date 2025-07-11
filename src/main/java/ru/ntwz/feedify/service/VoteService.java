package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.VoteDto;

public interface VoteService {

    VoteDto vote(Long postId, boolean isUpvote);

    VoteDto getUserVote(Long postId);
}
