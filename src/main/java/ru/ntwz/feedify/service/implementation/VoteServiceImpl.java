package ru.ntwz.feedify.service.implementation;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.dto.mapper.VoteMapper;
import ru.ntwz.feedify.dto.response.VoteDto;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.model.Vote;
import ru.ntwz.feedify.model.VoteType;
import ru.ntwz.feedify.repository.VoteRepository;
import ru.ntwz.feedify.service.PostService;
import ru.ntwz.feedify.service.VoteService;

@Service
@Slf4j
public class VoteServiceImpl implements VoteService {
    private final PostService postService;
    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(PostService postService, VoteRepository voteRepository) {
        this.postService = postService;
        this.voteRepository = voteRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "votes", key = "#postId")
    public VoteDto vote(Long postId, User user, boolean isUpvote) {
        Post post = postService.getPostOrThrow(postId);

        VoteType voteType = isUpvote ? VoteType.UPVOTE : VoteType.DOWNVOTE;
        Vote existingVote = voteRepository.findByUserAndPost(user, post).orElse(null);
        Vote resultVote = null;

        if (existingVote == null) {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setPost(post);
            vote.setVoteType(voteType);
            resultVote = voteRepository.save(vote);
            post.getVotes().add(resultVote);
        } else {
            if (existingVote.getVoteType() == voteType) {
                post.getVotes().remove(existingVote);
                voteRepository.delete(existingVote);
            } else {
                existingVote.setVoteType(voteType);
                resultVote = voteRepository.save(existingVote);
            }
        }

        log.info("User {} voted on post {}: {}", user.getUsername(), postId, voteType);

        return VoteMapper.toVoteDTO(post, resultVote);
    }

    @Override
    @Cacheable(value = "votes", key = "#postId")
    public VoteDto getUserVote(Long postId, User user) {
        Post post = postService.getPostOrThrow(postId);
        Vote vote = voteRepository.findByUserAndPost(user, post).orElse(null);
        return VoteMapper.toVoteDTO(post, vote);
    }
}