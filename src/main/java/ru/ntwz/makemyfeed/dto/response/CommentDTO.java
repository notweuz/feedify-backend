package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Integer rating;
    private UserDTO author;
    private Instant createdAt;
    //    private List<AttachmentDTO> attachments;
    private List<CommentDTO> comments;
    private Integer commentsCount;
    private Boolean isDeleted;
}
