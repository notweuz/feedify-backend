package ru.ntwz.feedify.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.constant.ValidationConstant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Size(min = ValidationConstant.Length.NAME_MIN, max = ValidationConstant.Length.NAME_MAX,
            message = "Username" + ValidationConstant.Message.WRONG_LENGTH)
    private String displayName;
    @Pattern(regexp = ValidationConstant.Regex.NAME_REGEX, message = "Username " + ValidationConstant.Message.INCORRECT_FORMAT)
    @Size(min = ValidationConstant.Length.NAME_MIN, max = ValidationConstant.Length.NAME_MAX,
            message = "Username" + ValidationConstant.Message.WRONG_LENGTH)
    private String username;
    @Size(max = ValidationConstant.Length.DESCRIPTION_MAX,
            message = "Description" + ValidationConstant.Message.WRONG_LENGTH)
    private String description;
}
