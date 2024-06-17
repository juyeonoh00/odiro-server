package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Member;
import odiro.domain.PlanMember;
import odiro.repository.MemberRepository;
import odiro.repository.PlanMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MemberRepository memberRepository;
    private final PlanMemberRepository planMemberRepository;

    private final MemberService memberService;

    public Optional<Plan> findById(Long planId) {
        return planRepository.findById(planId);
    }

    public List<Plan> findPlansByParticipantId(Long participantId) {
        List<PlanMember> planMembers = planMemberRepository.findByParticipantId(participantId);
        return planMembers.stream()
                .map(PlanMember::getPlan)
                .collect(Collectors.toList());
    }

    public Plan initPlanV2(Long memberId, String title, LocalDateTime firstDay, LocalDateTime lastDay) {
        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 플랜 생성
        Plan plan = new Plan(member, title, firstDay, lastDay);
        planRepository.save(plan);

        //플랜 멤버 저장
        PlanMember planMember = new PlanMember();
        planMember.setParticipant(member);
        planMember.setPlan(plan);
        planMemberRepository.save(planMember);

        //저장된 플랜 반환
        return plan;
    }
}
