package odiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DayPlanInDetailPage {
    private Long dayPlanId;
    private LocalDateTime date;
    private List<LocationInDetailPage> locations;
    private List<MemoInDetailPage> memos;
    private List<CommentInDetailPage> comments;
}
