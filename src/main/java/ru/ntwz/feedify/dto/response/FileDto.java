package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private byte[] data;
    private String name;
    private String contentType;
}
