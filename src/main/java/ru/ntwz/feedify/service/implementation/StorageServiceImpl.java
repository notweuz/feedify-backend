package ru.ntwz.feedify.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.mapper.StorageMapper;
import ru.ntwz.feedify.dto.response.FileDTO;
import ru.ntwz.feedify.dto.response.StorageEntryDTO;
import ru.ntwz.feedify.exception.*;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.PostRepository;
import ru.ntwz.feedify.repository.StorageRepository;
import ru.ntwz.feedify.repository.UserRepository;
import ru.ntwz.feedify.service.StorageService;
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

    private final CommonConfig commonConfig;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, CommonConfig commonConfig, UserRepository userRepository, PostRepository postRepository) {
        this.storageRepository = storageRepository;
        this.commonConfig = commonConfig;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
    public FileDTO getFileByUniqueName(String uniqueName) {
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
    public StorageEntry uploadFile(MultipartFile file, User user) {
        StorageEntry storageEntry = saveFileToStorage(file, user);
        return storageRepository.save(storageEntry);
    }

    @Override
    public StorageEntryDTO uploadAvatar(MultipartFile file, User user) {
        validateFile(file);
        validateFileType(file);

        StorageEntry storageEntry = saveFileToStorage(file, user);
        StorageEntry avatar = storageRepository.save(storageEntry);
        log.info("Avatar uploaded successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());
        user.setAvatar(avatar);
        userRepository.save(user);
        return StorageMapper.toDTO(storageEntry);
    }

    @Override
    public void deleteAvatar(User user) {
        if (user.getAvatar() == null) {
            log.warn("User {} has no avatar to delete", user.getUsername());
            return;
        }

        StorageEntry storageEntry = user.getAvatar();

        user.setAvatar(null);
        userRepository.save(user);

        Path filePath = Paths.get(storageEntry.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("Avatar deleted successfully: uniqueName={}, filePath={}", storageEntry.getUniqueName(), storageEntry.getFilePath());
        } catch (Exception e) {
            throw new FileReadingException("Error deleting file: " + filePath);
        }

        storageRepository.delete(storageEntry);
        userRepository.save(user);
    }

    @Override
    public StorageEntryDTO uploadBanner(MultipartFile file, User user) {
        validateFile(file);
        validateFileType(file);

        StorageEntry storageEntry = saveFileToStorage(file, user);
        StorageEntry banner = storageRepository.save(storageEntry);
        log.info("Banner uploaded successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());
        user.setBanner(banner);
        userRepository.save(user);
        return StorageMapper.toDTO(storageEntry);
    }

    @Override
    public void deleteBanner(User user) {
        if (user.getBanner() == null) {
            log.warn("User {} has no banner to delete", user.getUsername());
            return;
        }

        StorageEntry storageEntry = user.getBanner();

        user.setBanner(null);
        userRepository.save(user);

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
    public List<StorageEntryDTO> uploadTemporaryFiles(List<MultipartFile> files, User user) {
        if (files == null || files.isEmpty()) {
            throw new FilesCannotBeEmptyException("Files list cannot be null or empty");
        }
        if (files.size() > commonConfig.getContent().getMaxAttachments()) {
            throw new TooManyAttachmentsException("Too many attachments. Trying to add: " + files.size() +
                    ", maximum allowed: " + commonConfig.getContent().getMaxAttachments());
        }
        List<StorageEntryDTO> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                StorageEntry storageEntry = saveFileToStorage(file, user);
                storageEntry.setPost(null);
                StorageEntry savedEntry = storageRepository.save(storageEntry);
                log.info("Temporary file uploaded successfully: id={}, uniqueName={}, filePath={}, contentType={}, size={}",
                        savedEntry.getId(), storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());
                uploadedFiles.add(StorageMapper.toDTO(savedEntry));
            } catch (Exception e) {
                if (!uploadedFiles.isEmpty()) {
                    List<Long> uploadedIds = uploadedFiles.stream()
                            .map(StorageEntryDTO::getId)
                            .collect(Collectors.toList());
                    deleteTemporaryFilesByIds(uploadedIds, user);
                }
                throw e;
            }
        }
        log.info("Uploaded {} temporary files for user: {}", uploadedFiles.size(), user.getUsername());
        return uploadedFiles;
    }

    @Override
    public List<StorageEntry> getTemporaryFilesByIds(List<Long> attachmentIds, User user) {
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
    public void deleteTemporaryFiles(List<StorageEntry> temporaryFiles) {
        for (StorageEntry file : temporaryFiles) {
            try {
                Path filePath = Paths.get(file.getFilePath());
                Files.deleteIfExists(filePath);
                storageRepository.delete(file);
                log.info("Temporary file deleted successfully: id={}, uniqueName={}, filePath={}", 
                        file.getId(), file.getUniqueName(), file.getFilePath());
            } catch (Exception e) {
                log.error("Error deleting temporary file: id={}, filePath={}", file.getId(), file.getFilePath(), e);
            }
        }
    }

    @Override
    public void deleteTemporaryFilesByIds(List<Long> fileIds, User user) {
        List<StorageEntry> files = getTemporaryFilesByIds(fileIds, user);
        deleteTemporaryFiles(files);
    }

    @Override
    public void attachFilesToPost(List<StorageEntry> files, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        for (StorageEntry file : files) {
            file.setPost(post);
            storageRepository.save(file);
            log.info("File attached to post: fileId={}, postId={}", file.getId(), postId);
        }
    }
}
