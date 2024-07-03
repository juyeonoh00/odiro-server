package odiro.dto.dayPlan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import odiro.dto.location.LocationInDetailPage;
import odiro.dto.memo.MemoInDetailPage;
import odiro.dto.comment.CommentInDetailPage;

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
