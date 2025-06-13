package ru.ntwz.makemyfeed.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.mapper.StorageMapper;
import ru.ntwz.makemyfeed.dto.response.FileDTO;
import ru.ntwz.makemyfeed.dto.response.StorageEntryDTO;
import ru.ntwz.makemyfeed.exception.FileIsEmptyException;
import ru.ntwz.makemyfeed.exception.FileIsTooLargeException;
import ru.ntwz.makemyfeed.exception.FileNotFoundException;
import ru.ntwz.makemyfeed.exception.FileReadingException;
import ru.ntwz.makemyfeed.model.StorageEntry;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.StorageRepository;
import ru.ntwz.makemyfeed.repository.UserRepository;
import ru.ntwz.makemyfeed.service.StorageService;
import ru.ntwz.makemyfeed.util.RandUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    private final CommonConfig commonConfig;

    private final UserRepository userRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, CommonConfig commonConfig, UserRepository userRepository) {
        this.storageRepository = storageRepository;
        this.commonConfig = commonConfig;
        this.userRepository = userRepository;
    }

    private String getSavePath(String uniqueName) {
        return commonConfig.getContent().getStorage() + "/" + uniqueName;
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

    @Override
    public StorageEntryDTO uploadAvatar(MultipartFile file, User user) {
        if (file.isEmpty()) {
            throw new FileIsEmptyException("File is empty");
        }

        if (!file.getContentType().startsWith("image/") && !file.getContentType().equals("image/gif")) {
            throw new FileReadingException("File is not an image or GIF: " + file.getContentType());
        }

        if (file.getSize() > commonConfig.getContent().getMaxSize()) {
            throw new FileIsTooLargeException("File size exceeds the maximum allowed size, " + file.getSize() + " > " + commonConfig.getContent().getMaxSize() + " bytes");
        }

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
        storageRepository.save(storageEntry);

        log.info("File uploaded successfully: uniqueName={}, filePath={}, contentType={}, size={}",
                storageEntry.getUniqueName(), storageEntry.getFilePath(), storageEntry.getContentType(), storageEntry.getSize());

        user.setAvatarUrl(commonConfig.getPublicDomain() + "/storage/" + storageEntry.getUniqueName());
        userRepository.save(user);

        return StorageMapper.toDTO(storageEntry);
    }

    @Override
    public void deleteAvatar(User user) {
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
            log.warn("User {} has no avatar to delete", user.getUsername());
            return;
        }

        String uniqueName = user.getAvatarUrl().substring(user.getAvatarUrl().lastIndexOf("/") + 1);
        StorageEntry storageEntry = storageRepository.findByUniqueName(uniqueName)
                .orElseThrow(() -> new FileNotFoundException("File with unique name '" + uniqueName + "' not found"));

        Path filePath = Paths.get(storageEntry.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: uniqueName={}, filePath={}", uniqueName, storageEntry.getFilePath());
        } catch (Exception e) {
            throw new FileReadingException("Error deleting file: " + filePath);
        }

        storageRepository.delete(storageEntry);
        user.setAvatarUrl(null);
        userRepository.save(user);
    }
}
