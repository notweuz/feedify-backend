package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.constant.ValidationConstant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotNull(message = "Username" + ValidationConstant.Message.REQUIRED)
    private String username;
    @NotNull(message = "Password" + ValidationConstant.Message.REQUIRED)
    private String password;
}
