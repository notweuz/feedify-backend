package ru.ntwz.feedify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.response.StorageEntryDto;
import ru.ntwz.feedify.exception.FileIsEmptyException;
import ru.ntwz.feedify.exception.FileReadingException;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.StorageRepository;
import ru.ntwz.feedify.service.implementation.StorageServiceImpl;
import ru.ntwz.feedify.service.UserService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {
    @Mock
    private StorageRepository storageRepository;

    @Mock
    private CommonConfig commonConfig;

    @Mock
    private UserService userService;

    @InjectMocks
    private StorageServiceImpl storageService;

    @TempDir
    Path tempDir;

    private User user;

    private CommonConfig.Content content;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("<PASSWORD>");
        user.setDescription("test description");

        content = new CommonConfig.Content();
        content.setStorage("test-storage");
        content.setMaxSize(10 * 1024 * 1024);
        content.setMaxAttachments(5);
        content.setStorage(tempDir.toString());
    }

    @Test
    void uploadFile_shouldUploadFileSuccessfully() {
        when(commonConfig.getContent()).thenReturn(content);
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        StorageEntry storageEntry = new StorageEntry();
        storageEntry.setId(1L);

        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        try {
            when(mockFile.getBytes()).thenReturn(new byte[1024]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(storageRepository.save(Mockito.any(StorageEntry.class))).thenReturn(storageEntry);

        StorageEntry result = storageService.uploadFile(mockFile, user);

        assertThat(result).isNotNull();
        Mockito.verify(storageRepository).save(Mockito.any(StorageEntry.class));
        Mockito.verify(mockFile, Mockito.times(2)).getOriginalFilename();
        Mockito.verify(mockFile).getContentType();
        Mockito.verify(mockFile, Mockito.times(2)).getSize();
    }

    @Test
    void uploadFile_shouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        try {
            storageService.uploadFile(mockFile, user);
        } catch (FileIsEmptyException e) {
            assertThat(e.getMessage()).isEqualTo("File is empty");
        }

        Mockito.verify(storageRepository, Mockito.never()).save(Mockito.any(StorageEntry.class));
    }

    @Test
    void uploadAvatar_shouldUploadAvatarSuccessfully() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        StorageEntry storageEntry = new StorageEntry();
        storageEntry.setId(1L);

        when(mockFile.getOriginalFilename()).thenReturn("avatar.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(2048L);
        when(mockFile.isEmpty()).thenReturn(false);
        try {
            when(mockFile.getBytes()).thenReturn(new byte[2048]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(storageRepository.save(Mockito.any(StorageEntry.class))).thenReturn(storageEntry);

        StorageEntryDto result = storageService.uploadAvatar(mockFile, user);

        assertThat(result).isNotNull();
        Mockito.verify(storageRepository).save(Mockito.any(StorageEntry.class));
        Mockito.verify(userService).save(user);
    }

    @Test
    void uploadAvatar_shouldNotAllowUploadingAnythingButImage() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);

        try {
            storageService.uploadAvatar(mockFile, user);
        } catch (FileReadingException e) {
            assertThat(e.getMessage()).isEqualTo("File is not an image or GIF: text/plain");
        }

        Mockito.verify(storageRepository, Mockito.never()).save(Mockito.any(StorageEntry.class));
    }

    @Test
    void deleteAvatar_shouldDeleteAvatarIfExist() {
        StorageEntry avatar = new StorageEntry();
        avatar.setId(1L);
        avatar.setAuthor(user);
        avatar.setFilePath(tempDir.resolve("avatar.jpg").toString());
        user.setAvatar(avatar);
        assertThat(user.getAvatar()).isNotNull();

        storageService.deleteAvatar(user);

        Mockito.verify(storageRepository).delete(avatar);
        Mockito.verify(userService, Mockito.times(2)).save(user);
        assertThat(user.getAvatar()).isNull();
    }

    @Test
    void uploadBanner_shouldUploadBannerSuccessfully() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        StorageEntry storageEntry = new StorageEntry();
        storageEntry.setId(1L);

        when(mockFile.getOriginalFilename()).thenReturn("banner.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(4096L);
        when(mockFile.isEmpty()).thenReturn(false);
        try {
            when(mockFile.getBytes()).thenReturn(new byte[4096]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(storageRepository.save(Mockito.any(StorageEntry.class))).thenReturn(storageEntry);

        StorageEntryDto result = storageService.uploadBanner(mockFile, user);

        assertThat(result).isNotNull();
        Mockito.verify(storageRepository).save(Mockito.any(StorageEntry.class));
        Mockito.verify(userService).save(user);
    }

    @Test
    void uploadBanner_shouldNotAllowUploadingAnythingButImage() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);

        try {
            storageService.uploadBanner(mockFile, user);
        } catch (FileReadingException e) {
            assertThat(e.getMessage()).isEqualTo("File is not an image or GIF: text/plain");
        }

        Mockito.verify(storageRepository, Mockito.never()).save(Mockito.any(StorageEntry.class));
    }

    @Test
    void deleteBanner_shouldDeleteBannerIfExist() {
        StorageEntry banner = new StorageEntry();
        banner.setId(1L);
        banner.setAuthor(user);
        banner.setFilePath(tempDir.resolve("banner.jpg").toString());
        user.setBanner(banner);
        assertThat(user.getBanner()).isNotNull();

        storageService.deleteBanner(user);

        Mockito.verify(storageRepository).delete(banner);
        Mockito.verify(userService).save(user);
        assertThat(user.getBanner()).isNull();
    }

    @Test
    void attachFilesToPost_shouldAttachFilesToPostSuccessfully() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        StorageEntry storageEntry = new StorageEntry();
        storageEntry.setId(1L);

        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        StorageEntry storageEntry2 = new StorageEntry();
        storageEntry2.setId(2L);

        when(mockFile2.getOriginalFilename()).thenReturn("post_image2.jpg");
        when(mockFile2.getContentType()).thenReturn("image/jpeg");
        when(mockFile2.getSize()).thenReturn(2048L);
        when(mockFile2.isEmpty()).thenReturn(false);

        when(mockFile.getOriginalFilename()).thenReturn("post_image.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(2048L);
        when(mockFile.isEmpty()).thenReturn(false);
        try {
            when(mockFile.getBytes()).thenReturn(new byte[2048]);
            when(mockFile2.getBytes()).thenReturn(new byte[2048]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<StorageEntryDto> result = storageService.uploadTemporaryFiles(List.of(mockFile, mockFile2), user);

        assertThat(result).isNotNull();
        Mockito.verify(storageRepository).saveAll(Mockito.anyList());
    }

    @Test
    void uploadTemporaryFiles_shouldDeleteAllFilesIfSomeFileIsEmpty() {
        when(commonConfig.getContent()).thenReturn(content);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);

        when(mockFile.getOriginalFilename()).thenReturn("file1.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        try {
            when(mockFile.getBytes()).thenReturn(new byte[1024]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        when(mockFile2.isEmpty()).thenReturn(true);

        try {
            storageService.uploadTemporaryFiles(List.of(mockFile, mockFile2), user);
        } catch (FileIsEmptyException e) {
            assertThat(e.getMessage()).isEqualTo("File is empty");
        }

        Mockito.verify(storageRepository, Mockito.never()).saveAll(Mockito.anyList());
        Mockito.verify(storageRepository, Mockito.times(1)).deleteAll(Mockito.anyList());
    }
}
