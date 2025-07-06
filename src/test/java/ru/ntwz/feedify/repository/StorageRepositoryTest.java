package ru.ntwz.feedify.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StorageRepositoryTest {
    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void findByUniqueName_shouldReturnStorageEntryByItsUniqueName() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");
        entityManager.persist(user);

        StorageEntry attachment = new StorageEntry();
        attachment.setContentType("image/jpeg");
        attachment.setFilePath("/test/path");
        attachment.setSize(1024L);
        attachment.setAuthor(user);
        storageRepository.save(attachment);

        Optional<StorageEntry> foundStorageEntry = storageRepository.findByUniqueName(attachment.getUniqueName());

        assertThat(foundStorageEntry).isPresent();
        assertThat(foundStorageEntry.get().getContentType()).isEqualTo(attachment.getContentType());
        assertThat(foundStorageEntry.get().getFilePath()).isEqualTo(attachment.getFilePath());
        assertThat(foundStorageEntry.get().getSize()).isEqualTo(attachment.getSize());
        assertThat(foundStorageEntry.get().getAuthor()).isEqualTo(user);
        assertThat(foundStorageEntry.get().getUniqueName()).isEqualTo(attachment.getUniqueName());
        assertThat(foundStorageEntry.get().getId()).isEqualTo(attachment.getId());
    }
}
