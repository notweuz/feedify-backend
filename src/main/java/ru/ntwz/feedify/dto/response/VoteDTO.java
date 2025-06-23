package ru.ntwz.feedify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntwz.feedify.model.VoteType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long postId;
    private Long rating;
    private VoteType voteType;
}
