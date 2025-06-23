package ru.ntwz.feedify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ntwz.feedify.model.StorageEntry;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<StorageEntry, Long> {
    Optional<StorageEntry> findByUniqueName(String uniqueName);
}
