package odiro.dto.dayPlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.dto.location.LocationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayPlanDto {
    private Long id;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;
    private List<LocationDto> locationList; // LocationDto 리스트
}