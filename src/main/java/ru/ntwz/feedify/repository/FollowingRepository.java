package ru.ntwz.feedify.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntwz.feedify.model.Following;
import ru.ntwz.feedify.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    Optional<Following> findByFollowerAndFollowing(User follower, User following);

    List<Following> findByFollower(User follower, Pageable pageable);

    List<Following> findByFollowing(User following, Pageable pageable);

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);
}