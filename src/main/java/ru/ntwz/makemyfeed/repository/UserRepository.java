package ru.ntwz.makemyfeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntwz.makemyfeed.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
