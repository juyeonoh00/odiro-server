package odiro.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.Comment;
import odiro.domain.member.Authority;
import odiro.domain.member.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
    private Long commentId;
    private LocalDateTime writeTime;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.writeTime = comment.getWriteTime();
    }
}