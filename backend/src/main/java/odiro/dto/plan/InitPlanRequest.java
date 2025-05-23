package odiro.dto.plan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InitPlanRequest {

    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;
    private Boolean isPublic;
    private String planFilter;
}
