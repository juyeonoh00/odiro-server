package odiro.dto.plan;

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
public class GetDetailPlanResponse {
    private Long planId;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    private InitializerInDetailPage initializer;
    private List<MemberInDetailPage> participants;

    private List<DayPlanInDetailPage> dayPlans;
}
