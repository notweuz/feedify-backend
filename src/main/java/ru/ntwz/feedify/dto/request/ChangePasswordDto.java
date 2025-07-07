package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.constant.ValidationConstant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotNull(message = "Old password" + ValidationConstant.Message.REQUIRED)
    private String oldPassword;

    @NotNull
    @Size(min = ValidationConstant.Length.PASSWORD_MIN, max = ValidationConstant.Length.PASSWORD_MAX,
            message = "Password" + ValidationConstant.Message.WRONG_LENGTH)
    private String newPassword;
}