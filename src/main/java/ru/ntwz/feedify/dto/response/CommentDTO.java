package ru.ntwz.feedify.dto.response;

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
    private Long rating;
    private UserDTO author;
    private String uniqueLink;
    private Instant createdAt;
    private List<CommentDTO> comments;
    private Integer commentsCount;
    private List<PostAttachmentDTO> attachments;
    private Boolean isDeleted;
}
