package odiro.service;

import lombok.RequiredArgsConstructor;

import odiro.config.redis.RedisService;
import odiro.domain.member.Member;
import odiro.dto.member.HomeResponse;
import odiro.dto.plan.InitPlanRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.Plan;
import odiro.repository.PlanRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final MemberService memberService;
    private final RedisService redisService;

    public List<Member> findParticipantsByPlanId(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        List<Member> planMembers = plan.getPlanMembers();
//        List<PlanMember> planMembers = planMemberRepository.findByPlanId(planId);
//        List<Member> participants = planMembers.stream()
//                .map(PlanMember::getParticipant)
//                .collect(Collectors.toList());
        return planMembers;
    }

    public List<Plan> findPlansByParticipantId(Long participantId) {
        Member member = memberService.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + participantId));
        List<Plan> myPlanList = member.getPlans();
        return myPlanList;
    }

    public Plan initPlanV2(Long memberId, InitPlanRequest request) {
        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 플랜 생성
        Plan plan = new Plan();
        plan.initPlan(member, request.getTitle(), request.getLastDay(), request.getFirstDay(), request.getIsPublic(),request.getPlanFilter());
        planRepository.save(plan);

        // planFilter 저장
        if(plan.getIsPublic()){
            // planfilter가 key인 곳에 id를 삽입
            redisService.setList(String.valueOf(plan.getPlanFilter()), String.valueOf(plan.getId()));
        }

        //플랜 멤버 저장
        //저장된 플랜 반환
        return plan;
    }

    public Plan editPlan(Long planId, String title, LocalDateTime firstDay, LocalDateTime lastDay, Member member) {

        //Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        if(plan.getPlanMembers().contains(member)){
            //Plan 수정 후 저장
            plan.setTitle(title);
            plan.setFirstDay(firstDay);
            plan.setLastDay(lastDay);
            planRepository.save(plan);

            return plan;
        }
        else {
            throw new RuntimeException("userId not match with writerid: " + member.getId());
        }
    }

    public void deletePlan(Long planId, Member member) {

        // Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        if(plan.getPlanMembers().contains(member)) {
            // Plan 삭제
            planRepository.delete(plan);
        }
    }

    public List<HomeResponse> mapToHomeResponseList(List<Plan> planList) {

        List<HomeResponse> responses = new ArrayList<>();
        for (Plan plan : planList) {
            HomeResponse response = new HomeResponse(
                    plan.getId(), plan.getTitle(),plan.getFirstDay(), plan.getLastDay());
            responses.add(response);
        }
        return responses;
    }
}
