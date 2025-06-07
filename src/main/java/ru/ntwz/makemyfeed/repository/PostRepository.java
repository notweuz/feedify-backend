package ru.ntwz.makemyfeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntwz.makemyfeed.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
