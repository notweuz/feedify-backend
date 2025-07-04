package ru.ntwz.feedify.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        entityManager.clear();

        user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");
        entityManager.persist(user);
    }

    @Test
    void findByUniqueLink_shouldReturnPostByItsUniqueLink() {
        Post post = new Post();
        post.setContent("This is a test post.");
        post.setAuthor(user);
        postRepository.save(post);

        Optional<Post> foundPost = postRepository.findByUniqueLink(post.getUniqueLink());

        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getContent()).isEqualTo(post.getContent());
        assertThat(foundPost.get().getAuthor()).isEqualTo(user);
    }

    @Test
    void findAllActivePosts_shouldReturnAllNotDeletedPosts() {
        Post post = new Post();
        post.setContent("This is a test post.");
        post.setAuthor(user);
        postRepository.save(post);

        Post post2 = new Post();
        post2.setContent("This is another test post.");
        post2.setAuthor(user);
        post2.setIsDeleted(true);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAllActivePosts(Pageable.ofSize(10)).getContent();

        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getContent()).isEqualTo(post.getContent());
        assertThat(posts.getFirst().getAuthor()).isEqualTo(user);
    }

    @Test
    void findAllActivePosts_shouldReturnEmptyListIfAllPostsAreDeleted() {
        Post post = new Post();
        post.setContent("This is a test post.");
        post.setAuthor(user);
        postRepository.save(post);

        post.setIsDeleted(true); // actually it isn't deleting the post, entire post-deletion logic located in the service layer
        postRepository.save(post);

        List<Post> posts = postRepository.findAllActivePosts(Pageable.ofSize(10)).getContent();

        assertThat(posts).isEmpty();
    }

    @Test
    void findAllPostsWithAttachments_shouldReturnEmptyListIfThereIsNoPostsWithMedia() {
        Post post = new Post();
        post.setContent("This is a test post with no media.");
        post.setAuthor(user);
        postRepository.save(post);

        List<Post> posts = postRepository.findAllPostsWithAttachments(Pageable.ofSize(10)).getContent();

        assertThat(posts).isEmpty();
    }

    @Test
    void findAllPostsWithAttachments_shouldReturnPostsWithMedia() {
        StorageEntry attachment = new StorageEntry();
        attachment.setUniqueName("test_image.jpg");
        attachment.setContentType("image/jpeg");
        attachment.setSize(1024L);
        attachment.setAuthor(user);
        entityManager.persist(attachment);

        Post post = new Post();
        post.setContent("This is a test post with media.");
        post.setAuthor(user);
        post.setAttachments(List.of(attachment));
        postRepository.save(post);

        attachment.setPost(post);
        entityManager.persist(attachment);

        Post post2 = new Post();
        post2.setContent("This is another test post with no media.");
        post2.setAuthor(user);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAllPostsWithAttachments(Pageable.ofSize(10)).getContent();

        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getContent()).isEqualTo(post.getContent());
        assertThat(posts.getFirst().getAuthor()).isEqualTo(user);
    }

    @Test
    void findByAuthor_shouldCorrectlyReturnAllPostsMadeBySelectedAuthor() {
        Post post1 = new Post();
        post1.setContent("First post by user.");
        post1.setAuthor(user);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setContent("Second post by user.");
        post2.setAuthor(user);
        postRepository.save(post2);

        List<Post> posts = postRepository.findByAuthor(user, Pageable.ofSize(10)).getContent();

        assertThat(posts).hasSize(2);
        assertThat(posts).containsExactlyInAnyOrder(post1, post2);
    }
}
