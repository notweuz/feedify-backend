package ru.ntwz.makemyfeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntwz.makemyfeed.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
