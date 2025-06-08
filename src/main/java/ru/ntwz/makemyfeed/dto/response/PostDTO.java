package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private Long rating;
    private UserDTO author;
    private Instant createdAt;
    //    private List<AttachmentDTO> attachments;
    private List<CommentDTO> comments;
    private PostDTO parentPost;
    private Boolean isDeleted;
    private Integer commentsCount;
}
