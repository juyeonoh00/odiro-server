package odiro.dto.memo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MemoInDetailPage {
    private Long memberId;
    private String content;
}