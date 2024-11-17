package odiro.service;

import lombok.RequiredArgsConstructor;

import odiro.config.redis.RedisService;
import odiro.domain.PlanMember;
import odiro.domain.member.Member;
import odiro.dto.plan.InitPlanRequest;
import odiro.repository.PlanMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.Plan;
import odiro.repository.PlanRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;
    private final MemberService memberService;
    private final RedisService redisService;

    public Optional<Plan> findById(Long planId) {
        return planRepository.findById(planId);
    }

    public List<Member> findParticipantsByPlanId(Long planId) {
        List<PlanMember> planMembers = planMemberRepository.findByPlanId(planId);
        List<Member> participants = planMembers.stream()
                .map(PlanMember::getParticipant)
                .collect(Collectors.toList());
        return participants;
    }

    public List<Plan> findPlansByParticipantId(Long participantId) {
        List<PlanMember> planMembers = planMemberRepository.findByParticipantId(participantId);
        return planMembers.stream()
                .map(PlanMember::getPlan)
                .collect(Collectors.toList());
    }

    public Plan initPlanV2(Long memberId, InitPlanRequest request) {
        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 플랜 생성
        Plan plan = new Plan();
        plan.initPlan(member, request.getTitle(), request.getFirstDay(), request.getLastDay(), request.getIsPublic(),request.getPlanFilter());
        planRepository.save(plan);

        // planFilter 저장
        if(plan.getIsPublic()){
            // planfilter가 key인 곳에 id를 삽입
            redisService.setList(String.valueOf(plan.getPlanFilter()), String.valueOf(plan.getId()));
        }

        //플랜 멤버 저장
        PlanMember planMember = new PlanMember();
        planMember.setParticipant(member);
        planMember.setPlan(plan);
        planMemberRepository.save(planMember);

        //저장된 플랜 반환
        return plan;
    }

    public Plan editPlan(Long planId, String title, LocalDateTime firstDay, LocalDateTime lastDay, Long id) {

        //Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        if(plan.getInitializer().getId().equals(id)){
            //Plan 수정 후 저장
            plan.setTitle(title);
            plan.setFirstDay(firstDay);
            plan.setLastDay(lastDay);
            planRepository.save(plan);

            return plan;
        }
        else {
            throw new RuntimeException("userId not match with writerid: " + id);
        }
    }

    public void deletePlan(Long planId, Long id) {

        // Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        if(plan.getInitializer().getId().equals(id)) {
            // Plan 삭제
            planRepository.delete(plan);
        }
    }
}
