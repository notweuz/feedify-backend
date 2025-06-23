package ru.ntwz.feedify.service;

import ru.ntwz.feedify.dto.response.VoteDTO;
import ru.ntwz.feedify.model.User;

public interface VoteService {

    VoteDTO vote(Long postId, User user, boolean isUpvote);

    VoteDTO getUserVote(Long postId, User user);
}
