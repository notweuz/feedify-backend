package ru.ntwz.makemyfeed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntwz.makemyfeed.model.Subscription;
import ru.ntwz.makemyfeed.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByFollowerAndFollowing(User follower, User following);
    
    List<Subscription> findByFollower(User follower, Pageable pageable);
    
    List<Subscription> findByFollowing(User following, Pageable pageable);
    
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    void deleteByFollowerAndFollowing(User follower, User following);
} 