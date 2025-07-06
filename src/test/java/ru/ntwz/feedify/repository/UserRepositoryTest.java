package ru.ntwz.feedify.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;

import java.awt.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findById_shouldReturnUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");

        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
    }

    @Test
    void findByUsername_shouldReturnUserIfUserExists() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");

        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsername("testUser");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    void creatingAvatar_shouldCreateAvatarForUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");
        userRepository.save(user);

        StorageEntry avatar = new StorageEntry();
        avatar.setContentType("image/jpeg");
        avatar.setFilePath("/test/path");
        avatar.setSize(1024L);
        avatar.setAuthor(user);

        user.setAvatar(avatar);
        userRepository.save(user);

        assertThat(user.getAvatar()).isNotNull();
        assertThat(user.getAvatar()).isNotNull();
        assertThat(user.getAvatar().getContentType()).isEqualTo("image/jpeg");
        assertThat(user.getAvatar().getUniqueName()).isEqualTo(avatar.getUniqueName());
        assertThat(user.getAvatar().getAuthor()).isEqualTo(user);
        assertThat(user.getAvatar().getSize()).isEqualTo(1024L);
        assertThat(user.getAvatar().getFilePath()).isEqualTo("/test/path");
    }
}
