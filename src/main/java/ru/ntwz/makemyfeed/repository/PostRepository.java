package ru.ntwz.makemyfeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.User;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post AS p WHERE p.parentPost.id = :parentPostId ORDER BY p.createdAt DESC")
    Page<Post> findTop10CommentsByParentPostId(@Param("parentPostId") Long parentPostId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.parentPost.id = :parentPostId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findTopCommentsByParentPostId(@Param("parentPostId") Long parentPostId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author = :user AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> findByAuthor(User user, Pageable pageable);

    Optional<Post> findByUniqueLink(String uniqueLink);
}
