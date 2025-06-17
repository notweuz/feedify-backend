package ru.ntwz.makemyfeed.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.dto.response.FileDTO;
import ru.ntwz.makemyfeed.dto.response.StorageEntryDTO;
import ru.ntwz.makemyfeed.model.StorageEntry;
import ru.ntwz.makemyfeed.model.User;

import java.util.List;

public interface StorageService {
    FileDTO getFileByUniqueName(String uniqueName);

    StorageEntryDTO uploadAvatar(MultipartFile file, User user);

    StorageEntry uploadFile(MultipartFile file, User user);

    void deleteAvatar(User user);

    void deleteFile(StorageEntry storageEntry);

    List<StorageEntryDTO> uploadTemporaryFiles(List<MultipartFile> files, User user);

    List<StorageEntry> getTemporaryFilesByIds(List<Long> attachmentIds, User user);

    void deleteTemporaryFiles(List<StorageEntry> temporaryFiles);

    void deleteTemporaryFilesByIds(List<Long> fileIds, User user);

    void attachFilesToPost(List<StorageEntry> files, Long postId);
}
