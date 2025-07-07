package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long rating;
    private UserDto author;
    private String uniqueLink;
    private Instant createdAt;
    private List<CommentDto> comments;
    private Integer commentsCount;
    private List<PostAttachmentDto> attachments;
    private Boolean isDeleted;
}
