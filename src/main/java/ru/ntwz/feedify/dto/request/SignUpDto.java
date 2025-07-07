package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.constant.ValidationConstant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    @Size(min = ValidationConstant.Length.NAME_MIN, max = ValidationConstant.Length.NAME_MAX,
            message = "Display name" + ValidationConstant.Message.WRONG_LENGTH)
    private String displayName;

    @NotNull
    @Pattern(regexp = ValidationConstant.Regex.NAME_REGEX, message = "Username " + ValidationConstant.Message.INCORRECT_FORMAT)
    @Size(min = ValidationConstant.Length.NAME_MIN, max = ValidationConstant.Length.NAME_MAX,
            message = "Username" + ValidationConstant.Message.WRONG_LENGTH)
    private String username;

    @NotNull
    @Size(min = ValidationConstant.Length.PASSWORD_MIN, max = ValidationConstant.Length.PASSWORD_MAX,
            message = "Password" + ValidationConstant.Message.WRONG_LENGTH)
    private String password;
}