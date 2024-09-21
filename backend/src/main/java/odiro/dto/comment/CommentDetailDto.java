package odiro.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.Comment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDetailDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public static CommentDetailDto fromEntity(Comment comment) {
        return new CommentDetailDto(
                comment.getId(),
                comment.getContent(),
                comment.getWriter().getUsername(), // writer 이름 반환
                comment.getWriteTime()
        );
    }
}