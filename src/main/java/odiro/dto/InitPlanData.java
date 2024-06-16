package odiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InitPlanData {
    private Long memberId;
    private String title;
    private LocalDateTime firstday;
    private LocalDateTime lastday;
}
