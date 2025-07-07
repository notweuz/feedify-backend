package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String displayName;

    @NotNull
    private String username;

    @NotNull
    private String password;
}