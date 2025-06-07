package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.model.User;

public interface VoteService {

    void upvote(Long postId, User user);

    void downvote(Long postId, User user);
}
