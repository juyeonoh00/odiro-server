package odiro.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InitPlanRequest {
    private Long memberId;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;
}
