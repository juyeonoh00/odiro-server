package odiro.dto.dayPlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.dto.location.LocationDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayPlanDto {
    private String id;
    private String title;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private List<LocationDto> location; // LocationDto 리스트
}