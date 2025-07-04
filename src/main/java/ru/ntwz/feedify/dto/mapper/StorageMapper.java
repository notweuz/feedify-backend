package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.FileDTO;
import ru.ntwz.feedify.dto.response.PostAttachmentDTO;
import ru.ntwz.feedify.dto.response.StorageEntryDTO;
import ru.ntwz.feedify.model.StorageEntry;

public class StorageMapper {
    public static StorageEntryDTO toDTO(StorageEntry storageEntry) {
        StorageEntryDTO dto = new StorageEntryDTO();
        dto.setId(storageEntry.getId());
        dto.setContentType(storageEntry.getContentType());
        dto.setSize(storageEntry.getSize());
        dto.setFileName(storageEntry.getUniqueName());
        return dto;
    }

    public static FileDTO toDTO(byte[] content, String fileName, String contentType) {
        FileDTO dto = new FileDTO();
        dto.setData(content);
        dto.setName(fileName);
        dto.setContentType(contentType);
        return dto;
    }

    public static PostAttachmentDTO toPostAttachmentDTO(StorageEntry storageEntry, String fileUrl) {
        PostAttachmentDTO dto = new PostAttachmentDTO();
        dto.setId(storageEntry.getId());
        dto.setContentType(storageEntry.getContentType());
        dto.setFileUrl(fileUrl);
        return dto;
    }
}
