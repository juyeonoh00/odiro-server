package odiro.dto.plan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import odiro.dto.dayPlan.DayPlanInDetailPage;
import odiro.dto.member.InitializerInDetailPage;
import odiro.dto.member.MemberInDetailPage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetDetailPlanResponse {
    private Long id;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    private InitializerInDetailPage owner;
    private List<MemberInDetailPage> participant;

    private List<DayPlanInDetailPage> dayPlan;
}
