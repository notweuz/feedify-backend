package ru.ntwz.feedify.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.mapper.StorageMapper;
import ru.ntwz.feedify.dto.response.FileDto;
import ru.ntwz.feedify.dto.response.StorageEntryDto;
import ru.ntwz.feedify.exception.*;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.StorageRepository;
import ru.ntwz.feedify.service.StorageService;
import ru.ntwz.feedify.service.UserService;
import ru.ntwz.feedify.util.RandUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final UserService userService;
    private final CommonConfig commonConfig;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, UserService userService, CommonConfig commonConfig) {
        this.storageRepository = storageRepository;
        this.userService = userService;
        this.commonConfig = commonConfig;
    }


    private String getSavePath(String uniqueName) {
        return commonConfig.getContent().getStorage() + "/" + uniqueName;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileIsEmptyException("File is empty");
        }

        if (file.getSize() > commonConfig.getContent().getMaxSize()) {
            throw new FileIsTooLargeException("File size exceeds the maximum allowed size, " + file.getSize() + " > " + commonConfig.getContent().getMaxSize() + " bytes");
        }
    }

    private void validateFileType(MultipartFile file) {
        if (!file.getContentType().startsWith("image/") && !file.getContentType().equals("image/gif")) {
            throw new FileReadingException("File is not an image or GIF: " + file.getContentType());
        }
    }

    @Override
    @Cacheable(value = "files", key = "#uniqueName")
    public FileDto getFileByUniqueName(String uniqueName) {
        StorageEntry storageEntry = storageRepository.findByUniqueName(uniqueName)
                .orElseThrow(() -> new FileNotFoundException("File with unique name '" + uniqueName + "' not found"));

        Path filePath = Paths.get(storageEntry.getFilePath());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found at path: " + filePath);
        }

        byte[] fileContent = loadFile(storageEntry);

        log.info("File retrieved successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), fileContent.length);

        return StorageMapper.toDTO(fileContent, storageEntry.getUniqueName(), storageEntry.getContentType());
    }

    private byte[] loadFile(StorageEntry file) {
        try {
            return Files.readAllBytes(Paths.get(file.getFilePath()));
        } catch (Exception e) {
            throw new FileReadingException("Error reading file: " + file.getFilePath());
        }
    }

    private StorageEntry saveFileToStorage(MultipartFile file, User user) {
        validateFile(file);
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uniqueName = RandUtils.generateUniqueLink() + fileExtension;
        String filePath = getSavePath(uniqueName);
        try {
            Files.write(Paths.get(filePath), file.getBytes());
        } catch (Exception e) {
            throw new FileReadingException("Error saving file: " + filePath);
        }
        StorageEntry storageEntry = new StorageEntry();
        storageEntry.setUniqueName(uniqueName);
        storageEntry.setFilePath(filePath);
        storageEntry.setContentType(file.getContentType());
        storageEntry.setSize(file.getSize());
        storageEntry.setAuthor(user);
        return storageEntry;
    }

    @Override
    @CacheEvict(value = "files", key = "#result.uniqueName")
    public StorageEntry uploadFile(MultipartFile file) {
        User user = userService.getCurrentUser();
        StorageEntry storageEntry = saveFileToStorage(file, user);
        return storageRepository.save(storageEntry);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public StorageEntryDto uploadAvatar(MultipartFile file) {
        validateFile(file);
        validateFileType(file);
        User user = userService.getCurrentUser();

        StorageEntry storageEntry = saveFileToStorage(file, user);
        StorageEntry avatar = storageRepository.save(storageEntry);
        log.info("Avatar uploaded successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());
        user.setAvatar(avatar);
        userService.save(user);
        return StorageMapper.toDTO(storageEntry);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public void deleteAvatar() {
        User user = userService.getCurrentUser();
        if (user.getAvatar() == null) {
            log.warn("User {} has no avatar to delete", user.getUsername());
            return;
        }

        StorageEntry storageEntry = user.getAvatar();

        user.setAvatar(null);
        userService.save(user);

        Path filePath = Paths.get(storageEntry.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("Avatar deleted successfully: uniqueName={}, filePath={}", storageEntry.getUniqueName(), storageEntry.getFilePath());
        } catch (Exception e) {
            throw new FileReadingException("Error deleting file: " + filePath);
        }

        storageRepository.delete(storageEntry);
        userService.save(user);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public StorageEntryDto uploadBanner(MultipartFile file) {
        validateFile(file);
        validateFileType(file);
        User user = userService.getCurrentUser();

        StorageEntry storageEntry = saveFileToStorage(file, user);
        StorageEntry banner = storageRepository.save(storageEntry);
        log.info("Banner uploaded successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());
        user.setBanner(banner);
        userService.save(user);
        return StorageMapper.toDTO(storageEntry);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public void deleteBanner() {
        User user = userService.getCurrentUser();
        if (user.getBanner() == null) {
            log.warn("User {} has no banner to delete", user.getUsername());
            return;
        }

        StorageEntry storageEntry = user.getBanner();

        user.setBanner(null);
        userService.save(user);

        Path filePath = Paths.get(storageEntry.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("Banner deleted successfully: uniqueName={}, filePath={}", storageEntry.getUniqueName(), storageEntry.getFilePath());
        } catch (Exception e) {
            throw new FileReadingException("Error deleting file: " + filePath);
        }

        storageRepository.delete(storageEntry);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public void deleteFile(StorageEntry storageEntry) {
        Path filePath = Paths.get(storageEntry.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: uniqueName={}, filePath={}", storageEntry.getUniqueName(), storageEntry.getFilePath());
        } catch (Exception e) {
            throw new FileReadingException("Error deleting file: " + filePath);
        }

        storageRepository.delete(storageEntry);
    }

    @Override
    public void deleteFiles(List<StorageEntry> storageEntries) {
        if (storageEntries == null || storageEntries.isEmpty()) {
            log.warn("No files to delete");
            return;
        }

        for (StorageEntry storageEntry : storageEntries) {
            try {
                deleteFile(storageEntry);
            } catch (Exception e) {
                log.error("Error deleting file: uniqueName={}, filePath={}", storageEntry.getUniqueName(), storageEntry.getFilePath(), e);
            }
        }
        log.info("Deleted {} files", storageEntries.size());
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public List<StorageEntryDto> uploadTemporaryFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FilesCannotBeEmptyException("Files list cannot be null or empty");
        }
        if (files.size() > commonConfig.getContent().getMaxAttachments()) {
            throw new TooManyAttachmentsException("Too many attachments. Trying to add: " + files.size() +
                    ", maximum allowed: " + commonConfig.getContent().getMaxAttachments());
        }
        User user = userService.getCurrentUser();

        List<StorageEntry> storageEntries = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                StorageEntry storageEntry = saveFileToStorage(file, user);
                storageEntry.setPost(null);
                storageEntries.add(storageEntry);
            }
            List<StorageEntry> savedEntries = storageRepository.saveAll(storageEntries);
            log.info("Uploaded {} temporary files for user: {}", savedEntries.size(), user.getUsername());
            return savedEntries.stream()
                    .map(StorageMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            deleteTemporaryFiles(storageEntries);
            throw e;
        }
    }

    @Override
    @Cacheable(value = "temporaryFiles", key = "#attachmentIds.toString()")
    public List<StorageEntry> getTemporaryFilesByIds(List<Long> attachmentIds) {
        User user = userService.getCurrentUser();
        List<StorageEntry> files = storageRepository.findAllById(attachmentIds);

        List<StorageEntry> userTemporaryFiles = files.stream()
                .filter(file -> file.getAuthor().getId().equals(user.getId()) && file.getPost() == null)
                .collect(Collectors.toList());

        if (userTemporaryFiles.size() != attachmentIds.size()) {
            deleteTemporaryFiles(userTemporaryFiles);
            throw new FileNotFoundException("Some files not found or not accessible");
        }

        return userTemporaryFiles;
    }

    @Override
    @CacheEvict(value = "temporaryFiles", allEntries = true)
    public void deleteTemporaryFiles(List<StorageEntry> temporaryFiles) {
        if (temporaryFiles == null || temporaryFiles.isEmpty()) {
            return;
        }
        temporaryFiles.forEach(file -> {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
                log.info("Temporary file deleted successfully: id={}, uniqueName={}, filePath={}",
                        file.getId(), file.getUniqueName(), file.getFilePath());
            } catch (Exception e) {
                log.error("Error deleting temporary file: id={}, filePath={}", file.getId(), file.getFilePath(), e);
            }
        });
        storageRepository.deleteAll(temporaryFiles);
    }

    @Override
    @CacheEvict(value = "temporaryFiles", allEntries = true)
    public void deleteTemporaryFilesByIds(List<Long> fileIds) {
        List<StorageEntry> files = getTemporaryFilesByIds(fileIds);
        deleteTemporaryFiles(files);
    }

    @Override
    @CacheEvict(value = "files", allEntries = true)
    public void attachFilesToPost(List<StorageEntry> files, Post post) {
        for (StorageEntry file : files) {
            file.setPost(post);
        }
        List<StorageEntry> updatedFiles = storageRepository.saveAll(files);
        log.info("Attached {} files to post: postId={}", updatedFiles.size(), post.getId());
    }
}
