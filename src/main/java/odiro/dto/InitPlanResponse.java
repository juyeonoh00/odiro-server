package odiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class InitPlanResponse {

    private Long planId;
    private Long initializerId;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    public InitPlanResponse(Long planId, Long initializerId, String title, LocalDateTime firstDay, LocalDateTime lastDay) {
        this.planId = planId;
        this.initializerId = initializerId;
        this.title = title;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }
}
