package ru.ntwz.makemyfeed.service;

import ru.ntwz.makemyfeed.dto.response.VoteDTO;
import ru.ntwz.makemyfeed.model.User;

public interface VoteService {

    VoteDTO vote(Long postId, User user, boolean isUpvote);
}
