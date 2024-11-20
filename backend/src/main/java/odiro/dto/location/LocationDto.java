package odiro.dto.location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.dto.dayPlan.DayPlanDto;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private String lat;
    private String lng;
    private String imgUrl;
    private List<DayPlanDto> plans; // PlanDto 리스트
}