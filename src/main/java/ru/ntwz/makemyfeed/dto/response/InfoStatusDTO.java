package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoStatusDTO {
    private String status;
    private String version;
    private LocalDateTime serverTime = LocalDateTime.now();
}
