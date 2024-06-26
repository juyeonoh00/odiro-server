package odiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequest {

    private Long memberId;
    private String content;
    private LocalDateTime writeTime;
}
