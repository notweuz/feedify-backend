package ru.ntwz.makemyfeed.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    private String displayName;

    @NotNull
    private String username;

    @NotNull
    private String password;
}