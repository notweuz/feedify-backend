package ru.ntwz.feedify.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.ntwz.feedify.model.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

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
}
