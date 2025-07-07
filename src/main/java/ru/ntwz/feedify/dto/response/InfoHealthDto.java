package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoHealthDto {
    private String status;
    private String version;
    private LocalDateTime serverTime = LocalDateTime.now();
}
