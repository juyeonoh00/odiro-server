package odiro.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentInDetailPage {
    private Long id;
    private Long writerId;
    private String content;
    private LocalDateTime writtenTime;
}
