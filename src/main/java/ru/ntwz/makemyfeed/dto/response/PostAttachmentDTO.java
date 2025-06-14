package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAttachmentDTO {
    private Long id;
    private String contentType;
    private String fileUrl;
}
