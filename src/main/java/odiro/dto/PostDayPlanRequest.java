package odiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDayPlanRequest {
    private Long planId;
    private LocalDateTime day;
}
