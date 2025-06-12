package ru.ntwz.makemyfeed.service.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.dto.mapper.VoteMapper;
import ru.ntwz.makemyfeed.dto.response.VoteDTO;
import ru.ntwz.makemyfeed.exception.PostNotFoundException;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.model.Vote;
import ru.ntwz.makemyfeed.model.VoteType;
import ru.ntwz.makemyfeed.repository.PostRepository;
import ru.ntwz.makemyfeed.repository.VoteRepository;
import ru.ntwz.makemyfeed.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;

    public VoteServiceImpl(@Autowired PostRepository postRepository, @Autowired VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    @Transactional
    public VoteDTO vote(Long postId, User user, boolean isUpvote) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

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

        return VoteMapper.toVoteDTO(post, resultVote);
    }
}