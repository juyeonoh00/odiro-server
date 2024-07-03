package odiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
