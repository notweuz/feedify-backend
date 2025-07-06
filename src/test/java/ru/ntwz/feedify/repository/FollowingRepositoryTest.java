package ru.ntwz.feedify.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.ntwz.feedify.model.Following;
import ru.ntwz.feedify.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FollowingRepositoryTest {
    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private EntityManager entityManager;

    private User user1;

    private User user2;

    @BeforeEach
    void setUp() {
        entityManager.clear();

        user1 = new User();
        user1.setUsername("testUser1");
        user1.setPassword("<PASSWORD>");
        user1.setDescription("test description 1");
        entityManager.persist(user1);

        user2 = new User();
        user2.setUsername("testUser2");
        user2.setPassword("<PASSWORD>");
        user2.setDescription("test description 2");
        entityManager.persist(user2);
    }

    @Test
    void findByFollowerAndFollowing_shouldReturnFollowingIfExistsButShouldNotReturnViseVersa() {
        Following following = new Following();
        following.setFollower(user1);
        following.setFollowing(user2);
        followingRepository.save(following);

        Optional<Following> foundFollowing = followingRepository.findByFollowerAndFollowing(user1, user2);
        Optional<Following> foundReverseFollowing = followingRepository.findByFollowerAndFollowing(user2, user1);

        assertThat(foundFollowing).isPresent();
        assertThat(foundFollowing.get().getFollower()).isEqualTo(user1);
        assertThat(foundFollowing.get().getFollowing()).isEqualTo(user2);

        assertThat(foundReverseFollowing).isNotPresent();
        assertThat(followingRepository.existsByFollowerAndFollowing(user2, user1)).isFalse();
        assertThat(followingRepository.existsByFollowerAndFollowing(user1, user2)).isTrue();
    }

    @Test
    void findByFollowerAndFollowing_shouldReturnEmptyIfNotExists() {
        Optional<Following> foundFollowing = followingRepository.findByFollowerAndFollowing(user1, user2);
        assertThat(foundFollowing).isNotPresent();
    }

    @Test
    void findByFollowerAndFollowing_shouldReturnFollowingIfExistsAndShouldReturnViseVersa() {
        Following following = new Following();
        following.setFollower(user1);
        following.setFollowing(user2);
        followingRepository.save(following);

        Following following2 = new Following();
        following2.setFollower(user2);
        following2.setFollowing(user1);
        followingRepository.save(following2);

        Optional<Following> foundFollowing = followingRepository.findByFollowerAndFollowing(user1, user2);
        Optional<Following> foundReverseFollowing = followingRepository.findByFollowerAndFollowing(user2, user1);

        assertThat(foundFollowing).isPresent();
        assertThat(foundFollowing.get().getFollower()).isEqualTo(user1);
        assertThat(foundFollowing.get().getFollowing()).isEqualTo(user2);

        assertThat(foundReverseFollowing).isPresent();
        assertThat(foundReverseFollowing.get().getFollower()).isEqualTo(user2);
        assertThat(foundReverseFollowing.get().getFollowing()).isEqualTo(user1);
    }

    @Test
    void findByFollower_shouldReturnNoneIfFollowerHasNoFollowings() {
        List<Following> followings = followingRepository.findByFollower(user1, Pageable.ofSize(10));
        assertThat(followings).isEmpty();
    }

    @Test
    void findByFollower_shouldReturnFollowingsIfFollowerHasSomeFollowings() {
        User user3 = new User();
        user3.setUsername("testUser3");
        user3.setPassword("<PASSWORD>");
        user3.setDescription("test description 3");
        entityManager.persist(user3);

        User user4 = new User();
        user4.setUsername("testUser4");
        user4.setPassword("<PASSWORD>");
        user4.setDescription("test description 4");
        entityManager.persist(user4);

        Following following1 = new Following();
        following1.setFollower(user1);
        following1.setFollowing(user3);
        followingRepository.save(following1);

        Following following2 = new Following();
        following2.setFollower(user1);
        following2.setFollowing(user4);
        followingRepository.save(following2);

        List<Following> followings = followingRepository.findByFollower(user1, Pageable.ofSize(10));
        assertThat(followings).isNotEmpty();
        assertThat(followings).hasSize(2);
        assertThat(followings.get(0).getFollower()).isEqualTo(user1);
        assertThat(followings.get(0).getFollowing()).isIn(user3, user4);
        assertThat(followings.get(1).getFollower()).isEqualTo(user1);
        assertThat(followings.get(1).getFollowing()).isIn(user3, user4);
        assertThat(followings.get(0).getFollowing()).isNotEqualTo(followings.get(1).getFollowing());
    }

    @Test
    void findByFollower_paginationShouldWork() {
        User user3 = new User();
        user3.setUsername("testUser3");
        user3.setPassword("<PASSWORD>");
        user3.setDescription("test description 3");
        entityManager.persist(user3);

        Following following1 = new Following();
        following1.setFollower(user1);
        following1.setFollowing(user3);
        followingRepository.save(following1);

        Following following2 = new Following();
        following2.setFollower(user1);
        following2.setFollowing(user2);
        followingRepository.save(following2);

        List<Following> followingsPage1 = followingRepository.findByFollower(user1, Pageable.ofSize(1));
        List<Following> followingsPage2 = followingRepository.findByFollower(user1, Pageable.ofSize(1).withPage(1));

        assertThat(followingsPage1).hasSize(1);
        assertThat(followingsPage1.get(0).getFollower()).isEqualTo(user1);
        assertThat(followingsPage2).hasSize(1);
        assertThat(followingsPage2.get(0).getFollower()).isEqualTo(user1);
    }

    @Test
    void findByFollowing_shouldReturnNoneIfFollowingHasNoFollowers() {
        List<Following> followers = followingRepository.findByFollowing(user1, Pageable.ofSize(10));
        assertThat(followers).isEmpty();
    }

    @Test
    void findByFollowing_shouldReturnFollowersIfFollowingHasSomeFollowers() {
        User user3 = new User();
        user3.setUsername("testUser3");
        user3.setPassword("<PASSWORD>");
        user3.setDescription("test description 3");
        entityManager.persist(user3);

        User user4 = new User();
        user4.setUsername("testUser4");
        user4.setPassword("<PASSWORD>");
        user4.setDescription("test description 4");
        entityManager.persist(user4);

        Following following1 = new Following();
        following1.setFollower(user3);
        following1.setFollowing(user1);
        followingRepository.save(following1);

        Following following2 = new Following();
        following2.setFollower(user4);
        following2.setFollowing(user1);
        followingRepository.save(following2);

        List<Following> followers = followingRepository.findByFollowing(user1, Pageable.ofSize(10));
        assertThat(followers).isNotEmpty();
        assertThat(followers).hasSize(2);
        assertThat(followers.get(0).getFollowing()).isEqualTo(user1);
        assertThat(followers.get(0).getFollower()).isIn(user3, user4);
        assertThat(followers.get(1).getFollowing()).isEqualTo(user1);
        assertThat(followers.get(1).getFollower()).isIn(user3, user4);
        assertThat(followers.get(0).getFollower()).isNotEqualTo(followers.get(1).getFollower());
    }

    @Test
    void findByFollowing_paginationShouldWork() {
        User user3 = new User();
        user3.setUsername("testUser3");
        user3.setPassword("<PASSWORD>");
        user3.setDescription("test description 3");
        entityManager.persist(user3);

        Following following1 = new Following();
        following1.setFollower(user3);
        following1.setFollowing(user1);
        followingRepository.save(following1);

        Following following2 = new Following();
        following2.setFollower(user2);
        following2.setFollowing(user1);
        followingRepository.save(following2);

        List<Following> followersPage1 = followingRepository.findByFollowing(user1, Pageable.ofSize(1));
        List<Following> followersPage2 = followingRepository.findByFollowing(user1, Pageable.ofSize(1).withPage(1));

        assertThat(followersPage1).hasSize(1);
        assertThat(followersPage1.get(0).getFollowing()).isEqualTo(user1);
        assertThat(followersPage2).hasSize(1);
        assertThat(followersPage2.get(0).getFollowing()).isEqualTo(user1);
    }

    @Test
    void existsByFollowerAndFollowing_shouldReturnTrueIfFollowingExists() {
        Following following = new Following();
        following.setFollower(user1);
        following.setFollowing(user2);
        followingRepository.save(following);

        boolean exists = followingRepository.existsByFollowerAndFollowing(user1, user2);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByFollowerAndFollowing_shouldReturnFalseIfFollowingDoesNotExist() {
        boolean exists = followingRepository.existsByFollowerAndFollowing(user1, user2);
        assertThat(exists).isFalse();
    }

    @Test
    void deleteByFollowerAndFollowing_shouldDeleteFollowing() {
        Following following = new Following();
        following.setFollower(user1);
        following.setFollowing(user2);
        followingRepository.save(following);

        followingRepository.deleteByFollowerAndFollowing(user1, user2);

        Optional<Following> foundFollowing = followingRepository.findByFollowerAndFollowing(user1, user2);
        assertThat(foundFollowing).isNotPresent();
        boolean exists = followingRepository.existsByFollowerAndFollowing(user1, user2);
        assertThat(exists).isFalse();
    }

    @Test
    void deleteByFollowerAndFollowing_shouldDeleteOnlyOneDirectionFollowing() {
        Following following1 = new Following();
        following1.setFollower(user1);
        following1.setFollowing(user2);
        followingRepository.save(following1);

        Following following2 = new Following();
        following2.setFollower(user2);
        following2.setFollowing(user1);
        followingRepository.save(following2);

        followingRepository.deleteByFollowerAndFollowing(user1, user2);

        Optional<Following> foundFollowing1 = followingRepository.findByFollowerAndFollowing(user1, user2);
        Optional<Following> foundFollowing2 = followingRepository.findByFollowerAndFollowing(user2, user1);

        assertThat(foundFollowing1).isNotPresent();
        assertThat(foundFollowing2).isPresent();
    }
}
