package odiro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.domain.member.Member;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
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

    //완전한 DayPlan 객체를 저장할때 사용
    public DayPlan save(DayPlan dayPlan) {
        return dayPlanRepository.save(dayPlan);
    }


    public Optional<DayPlan> findById(Long dayPlanId) {
        return dayPlanRepository.findById(dayPlanId);
    }

    public DayPlan reorderLocations(Long dayPlanId, List<Long> orderedLocationIds, Long planId, Member member) {
        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId)
                .orElseThrow(() -> new CustomException(ErrorCode.DAYPLAN_NOT_FOUND, dayPlanId));


        if(!dayPlan.getPlan().getPlanMembers().stream().anyMatch(pm->pm.getParticipant().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, member.getId());

        } else if (!dayPlan.getPlan().getId().equals(planId)) {
            throw new CustomException(ErrorCode.INVALID_PLAN_ID, planId);

        } else {
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
}
