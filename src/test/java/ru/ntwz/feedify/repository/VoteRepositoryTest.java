package ru.ntwz.feedify.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.model.Vote;
import ru.ntwz.feedify.model.VoteType;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class VoteRepositoryTest {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByUserAndPost_shouldReturnValidCreatedVote() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");
        entityManager.persist(user);

        Post post = new Post();
        post.setContent("This is a test post.");
        post.setAuthor(user);
        entityManager.persist(post);

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPost(post);
        vote.setVoteType(VoteType.UPVOTE);
        entityManager.persist(vote);

        Optional<Vote> foundVote = voteRepository.findByUserAndPost(user, post);
        assertThat(foundVote).isPresent();
    }

    @Test
    void findByUserAndPost_shouldReturnNothingAfterDeletingVote() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");
        entityManager.persist(user);

        Post post = new Post();
        post.setContent("This is a test post.");
        post.setAuthor(user);
        entityManager.persist(post);

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setPost(post);
        vote.setVoteType(VoteType.UPVOTE);
        entityManager.persist(vote);

        Optional<Vote> foundVote = voteRepository.findByUserAndPost(user, post);
        assertThat(foundVote).isPresent();

        voteRepository.delete(vote);

        foundVote = voteRepository.findByUserAndPost(user, post);
        assertThat(foundVote).isEmpty();
    }
}
