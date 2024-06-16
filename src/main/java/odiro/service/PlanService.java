package odiro.service;

import odiro.domain.Member;
import odiro.domain.PlanMember;
import odiro.repository.PlanMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.Plan;
import odiro.repository.PlanRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanService {

    @Autowired
    private PlanRepository planRepository;
    private PlanMemberRepository planMemberRepository;

    public Plan initPlan(Plan plan) {
        planRepository.save(plan);
        return plan;
    }

    public Optional<Plan> findById(Long planId) {
        return planRepository.findById(planId);
    }

    public List<Plan> findPlansByParticipantId(Long participantId) {
        List<PlanMember> planMembers = planMemberRepository.findByParticipantId(participantId);
        return planMembers.stream()
                .map(PlanMember::getPlan)
                .collect(Collectors.toList());
    }
}
