package ru.ntwz.feedify.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.User;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post AS p WHERE p.parentPost.id = :parentPostId ORDER BY p.createdAt DESC")
    Page<Post> findTop10CommentsByParentPostId(@Param("parentPostId") Long parentPostId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.parentPost.id = :parentPostId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findTopCommentsByParentPostId(@Param("parentPostId") Long parentPostId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author = :user AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findByAuthor(User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author IN (SELECT f.follower FROM Following f WHERE f.following = :user) AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findPostsByFollowingUsers(@Param("user") User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findAllActivePosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND SIZE(p.attachments) > 0 ORDER BY p.createdAt DESC")
    Page<Post> findAllPostsWithAttachments(Pageable pageable);

    @Query(value = "SELECT p.* FROM posts p WHERE p.is_deleted = false AND p.created_at >= CURRENT_DATE - INTERVAL '31 days' ORDER BY (SELECT COUNT(v.id) FROM votes v WHERE v.post_id = p.id AND v.vote_type = 'UPVOTE') DESC, (SELECT COUNT(c.id) FROM posts c WHERE c.parent_post_id = p.id) DESC", nativeQuery = true)
    Page<Post> findTopPostsByRatingAndCommentsMonthly(Pageable pageable);

    @Query("""
                SELECT p FROM Post p
                WHERE p.author IN (
                    SELECT f1.following FROM Following f1 WHERE f1.follower = :user
                    UNION
                    SELECT f2.following FROM Following f2 WHERE f2.follower IN (
                        SELECT f1.following FROM Following f1 WHERE f1.follower = :user
                    )
                )
                AND p.isDeleted = false
                ORDER BY p.createdAt DESC
            """)
    Page<Post> findPostsByFollowingAndTheirFollowings(@Param("user") User user, Pageable pageable);

    // TODO: implement better feed algorithms

    @Query("""
        SELECT p FROM Post p
        WHERE p.id IN (
            SELECT v.post.id FROM Vote v
            WHERE v.user IN (
                SELECT f.following FROM Following f WHERE f.follower = :user
            )
        )
        AND p.isDeleted = false
        ORDER BY p.createdAt DESC
    """)
    Page<Post> findPostsLikedByFollowedUsers(@Param("user") User user, Pageable pageable);

    Optional<Post> findByUniqueLink(String uniqueLink);
}
