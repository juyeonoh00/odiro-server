package odiro.dto.dayPlan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DayPlanInDetailPage {
    private Long id;
    private LocalDateTime date;
    private List<LocationInDetailPage> location;
    private List<MemoInDetailPage> memo;
    private List<CommentInDetailPage> comment;
}
