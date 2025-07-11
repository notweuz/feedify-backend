package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.VoteDto;
import ru.ntwz.feedify.model.User;

public interface VoteService {

    VoteDto vote(Long postId, boolean isUpvote);

    VoteDto getUserVote(Long postId);
}
