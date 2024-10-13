package odiro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.domain.member.Member;
import odiro.repository.DayPlanRepository;
import odiro.repository.PlanRepository;
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
    private final PlanRepository planRepository;

    //완전한 DayPlan 객체를 저장할때 사용
    public DayPlan save(DayPlan dayPlan) {
        return dayPlanRepository.save(dayPlan);
    }

    public DayPlan postDayPlan(Long planId, LocalDateTime day) {


        //Optional
        Plan plan = planRepository.findById(planId)
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

    public DayPlan reorderLocations(Long dayPlanId, List<Long> orderedLocationIds, Long planId, Member member) {
        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId)
                .orElseThrow(() -> new EntityNotFoundException("DayPlan not found"));


        if(dayPlan.getPlan().getId().equals(planId) && dayPlan.getPlan().getPlanMembers().contains(member)) {
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
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }
}
