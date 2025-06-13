package ru.ntwz.makemyfeed.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.dto.response.FileDTO;
import ru.ntwz.makemyfeed.dto.response.StorageEntryDTO;
import ru.ntwz.makemyfeed.model.User;

public interface StorageService {
    FileDTO getFileByUniqueName(String uniqueName);

    StorageEntryDTO uploadAvatar(MultipartFile file, User user);

    void deleteAvatar(User user);
}
