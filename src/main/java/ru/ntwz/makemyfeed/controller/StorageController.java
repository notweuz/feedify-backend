package ru.ntwz.makemyfeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.dto.response.FileDTO;
import ru.ntwz.makemyfeed.dto.response.StorageEntryDTO;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.StorageService;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/avatar")
    public StorageEntryDTO uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestAttribute(AttributesConstants.USER) User user
    ) {
        return storageService.uploadAvatar(file, user);
    }

    @DeleteMapping("/avatar")
    public void deleteAvatar(
            @RequestAttribute(AttributesConstants.USER) User user
    ) {
        storageService.deleteAvatar(user);
    }

    @GetMapping("/{uniqueName}")
    public ResponseEntity<byte[]> getFileByUniqueName(
            @PathVariable String uniqueName
    ) {
        FileDTO imageData = storageService.getFileByUniqueName(uniqueName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(imageData.getName())
                        .build()
        );
        headers.setContentType(MediaType.parseMediaType(imageData.getContentType()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageData.getData());
    }
}