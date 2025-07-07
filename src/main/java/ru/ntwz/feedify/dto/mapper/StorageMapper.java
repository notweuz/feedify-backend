package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.FileDto;
import ru.ntwz.feedify.dto.response.PostAttachmentDto;
import ru.ntwz.feedify.dto.response.StorageEntryDto;
import ru.ntwz.feedify.model.StorageEntry;

public class StorageMapper {
    public static StorageEntryDto toDTO(StorageEntry storageEntry) {
        StorageEntryDto dto = new StorageEntryDto();
        dto.setId(storageEntry.getId());
        dto.setContentType(storageEntry.getContentType());
        dto.setSize(storageEntry.getSize());
        dto.setFileName(storageEntry.getUniqueName());
        return dto;
    }

    public static FileDto toDTO(byte[] content, String fileName, String contentType) {
        FileDto dto = new FileDto();
        dto.setData(content);
        dto.setName(fileName);
        dto.setContentType(contentType);
        return dto;
    }

    public static PostAttachmentDto toPostAttachmentDTO(StorageEntry storageEntry, String fileUrl) {
        PostAttachmentDto dto = new PostAttachmentDto();
        dto.setId(storageEntry.getId());
        dto.setContentType(storageEntry.getContentType());
        dto.setFileUrl(fileUrl);
        return dto;
    }
}
