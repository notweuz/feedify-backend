package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.constant.ValidationConstant;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {
    @Size(max = ValidationConstant.Length.POST_CONTENT_MAX,
            message = "Post content" + ValidationConstant.Message.WRONG_LENGTH)
    private String content;
    private List<Long> attachments;
}
