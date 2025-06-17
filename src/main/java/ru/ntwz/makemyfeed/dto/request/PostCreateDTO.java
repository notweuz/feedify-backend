package ru.ntwz.makemyfeed.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO {
    private String content;
    private List<Long> attachments;
}
