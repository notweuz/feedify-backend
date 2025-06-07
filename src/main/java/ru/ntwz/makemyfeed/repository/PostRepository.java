package ru.ntwz.makemyfeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ntwz.makemyfeed.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post AS p WHERE p.parentPost.id = :parentPostId ORDER BY p.createdAt ASC")
    Page<Post> findTop10CommentsByParentPostId(@Param("parentPostId") Long parentPostId, Pageable pageable);
}
