package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.model.User;

public interface VoteService {

    void vote(Long postId, User user, boolean isUpvote);
}
