package ru.ntwz.makemyfeed.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntwz.makemyfeed.exception.AlreadyVotedForPostException;
import ru.ntwz.makemyfeed.exception.PostNotFoundException;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.PostRepository;
import ru.ntwz.makemyfeed.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

    private final PostRepository postRepository;

    public VoteServiceImpl(@Autowired PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    @Override
    public void upvote(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

        if (!post.getLikedByUsers().contains(user)) {
            post.getLikedByUsers().add(user);
            post.getDislikedByUsers().remove(user);
            postRepository.save(post);
        } else {
            throw new AlreadyVotedForPostException("User has already upvoted this post");
        }
    }

    @Override
    public void downvote(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

        if (!post.getDislikedByUsers().contains(user)) {
            post.getDislikedByUsers().add(user);
            post.getLikedByUsers().remove(user);
            postRepository.save(post);
        } else {
            throw new AlreadyVotedForPostException("User has already downvoted this post");
        }
    }
}