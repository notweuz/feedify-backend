package ru.ntwz.feedify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.model.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndPost(User user, Post post);

    void delete(Vote vote);
}