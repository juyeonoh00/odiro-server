package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Plan;
import odiro.repository.DayPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

}
