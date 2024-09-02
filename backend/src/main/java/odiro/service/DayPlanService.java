package odiro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.repository.DayPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DayPlanService {

    private final DayPlanRepository dayPlanRepository;
    private final PlanService planService;

    public DayPlan postDayPlan(Long planId, LocalDateTime day) {

        //Optional
        Plan plan = planService.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));

        DayPlan savedDayPlan = new DayPlan();
        savedDayPlan.setPlan(plan);
        savedDayPlan.setDate(day);

        dayPlanRepository.save(savedDayPlan);
        return savedDayPlan;
    }

    public Optional<DayPlan> findById(Long dayPlanId) {
        return dayPlanRepository.findById(dayPlanId);
    }

    public DayPlan reorderLocations(Long dayPlanId, List<Long> orderedLocationIds, Long planId, Long userId) {
        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId)
                .orElseThrow(() -> new EntityNotFoundException("DayPlan not found"));

        List<Location> locations = dayPlan.getLocations();

        // 새로운 순서로 Location 리스트 재정렬
        List<Location> sortedLocations = new ArrayList<>();
        for (Long locationId : orderedLocationIds) {
            locations.stream()
                    .filter(location -> location.getId().equals(locationId))
                    .findFirst()
                    .ifPresent(sortedLocations::add);
        }

        // 기존 Location 리스트 비우고 정렬된 리스트로 채우기
        locations.clear();
        locations.addAll(sortedLocations);

        return dayPlan;
    }
}
