package ru.ntwz.makemyfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.makemyfeed.model.VoteType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long postId;
    private Long rating;
    private VoteType voteType;
}
