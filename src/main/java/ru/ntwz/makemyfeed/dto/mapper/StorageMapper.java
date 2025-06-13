package ru.ntwz.makemyfeed.dto.mapper;

import ru.ntwz.makemyfeed.dto.response.FileDTO;
import ru.ntwz.makemyfeed.dto.response.StorageEntryDTO;
import ru.ntwz.makemyfeed.model.StorageEntry;

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
}
