package ru.ntwz.feedify.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.feedify.dto.response.FileDto;
import ru.ntwz.feedify.dto.response.StorageEntryDto;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.Post;

import java.util.List;

public interface StorageService {
    FileDto getFileByUniqueName(String uniqueName);

    StorageEntryDto uploadAvatar(MultipartFile file);

    StorageEntry uploadFile(MultipartFile file);

    void deleteAvatar();

    void deleteFile(StorageEntry storageEntry);

    void deleteFiles(List<StorageEntry> storageEntries);

    List<StorageEntryDto> uploadTemporaryFiles(List<MultipartFile> files);

    List<StorageEntry> getTemporaryFilesByIds(List<Long> attachmentIds);

    void deleteTemporaryFiles(List<StorageEntry> temporaryFiles);

    void deleteTemporaryFilesByIds(List<Long> fileIds);

    void attachFilesToPost(List<StorageEntry> files, Post post);

    StorageEntryDto uploadBanner(MultipartFile file);

    void deleteBanner();
}
