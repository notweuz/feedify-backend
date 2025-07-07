package ru.ntwz.feedify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.feedify.constant.AttributesConstant;
import ru.ntwz.feedify.dto.response.FileDto;
import ru.ntwz.feedify.dto.response.StorageEntryDto;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/avatar")
    public StorageEntryDto uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestAttribute(AttributesConstant.USER) User user
    ) {
        return storageService.uploadAvatar(file, user);
    }

    @DeleteMapping("/avatar")
    public void deleteAvatar(
            @RequestAttribute(AttributesConstant.USER) User user
    ) {
        storageService.deleteAvatar(user);
    }

    @PostMapping("/banner")
    public StorageEntryDto uploadBanner(
            @RequestParam("file") MultipartFile file,
            @RequestAttribute(AttributesConstant.USER) User user
    ) {
        return storageService.uploadBanner(file, user);
    }

    @DeleteMapping("/banner")
    public void deleteBanner(
            @RequestAttribute(AttributesConstant.USER) User user
    ) {
        storageService.deleteBanner(user);
    }

    @GetMapping("/{uniqueName}")
    public ResponseEntity<byte[]> getFileByUniqueName(
            @PathVariable String uniqueName
    ) {
        FileDto imageData = storageService.getFileByUniqueName(uniqueName);

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

    @PostMapping("/temporary")
    public Object uploadTemporaryFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestAttribute(AttributesConstant.USER) User user
    ) {
        return storageService.uploadTemporaryFiles(files, user);
    }

    @DeleteMapping("/temporary")
    public void deleteTemporaryFiles(
            @RequestAttribute(AttributesConstant.USER) User user,
            @RequestBody List<Long> fileIds
    ) {
        storageService.deleteTemporaryFilesByIds(fileIds, user);
    }
}