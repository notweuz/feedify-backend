package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private Long rating;
    private UserShortDto author;
    private Instant createdAt;
    private String uniqueLink;
    private List<PostAttachmentDto> attachments;
    private PostDto parentPost;
    private Boolean isDeleted;
    private Integer commentsCount;
}
