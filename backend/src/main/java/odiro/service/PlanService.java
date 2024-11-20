package odiro.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import odiro.config.redis.RedisService;
import odiro.domain.DayPlan;
import odiro.domain.PlanMember;
import odiro.domain.member.Member;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.dto.location.LocationDto;
import odiro.dto.plan.InitPlanRequest;
import odiro.dto.plan.PlanInvitationListResponse;
import odiro.repository.MemberRepository;
import odiro.repository.PlanInvitationRepository;
import odiro.repository.PlanMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.Plan;
import odiro.domain.PlanInvitation;
import odiro.repository.PlanRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;
    private final MemberRepository memberRepository;
    private final PlanInvitationRepository planInvitationRepository;
    private final MemberService memberService;
    private final RedisService redisService;
    private final HomeService homeService;

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


    public Plan initPlanV2(Long memberId, InitPlanRequest request) {
        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 플랜 생성
        Plan plan = new Plan();
        plan.initPlan(member, request.getTitle(),  request.getFirstDay(),request.getLastDay(), request.getIsPublic(),request.getPlanFilter());

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

    public Plan editPlan(Long planId, String title, LocalDateTime firstDay, LocalDateTime lastDay, Member member) {


        //Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));

        if(plan.getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(member.getId()))) {
            //Plan 수정 후 저장
            plan.setTitle(title);
            plan.setFirstDay(firstDay);
            plan.setLastDay(lastDay);
            planRepository.save(plan);

            return plan;
        }
        else {
            throw new RuntimeException("userId not match with writerid: " + member);
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

    // 초대 생성
    public PlanInvitation inviteMember(Long senderId, Long planId, Long receiverId) {
        // 초대 생성에 필요한 멤버 및 플랜 확인
        Member sender = memberService.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Member receiver = memberService.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // 초대 생성 및 저장
        PlanInvitation invitation = PlanInvitation.builder()
                .plan(plan)
                .sender(sender)
                .receiver(receiver)
                .isAccepted(false)
                .build();
        return planInvitationRepository.save(invitation);
    }

    public List<PlanInvitationListResponse> getPendingInvitations(Long receiverId) {
        List<PlanInvitation> invitations = planInvitationRepository.findByReceiverIdAndIsAcceptedFalse(receiverId);

        // 필요한 정보를 DTO로 변환
        return invitations.stream()
                .map(invitation -> new PlanInvitationListResponse(
                        invitation.getPlan().getId(),
                        invitation.getPlan().getTitle()
                ))
                .collect(Collectors.toList());
    }

    public void acceptInvitation(Long memberId, Long planId) {
        // Plan과 Member 존재 여부 확인
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        // PlanMember에 추가
        PlanMember planMember = new PlanMember();
        planMember.setParticipant(member);
        planMember.setPlan(plan);
        planMemberRepository.save(planMember);

        // PlanInvitation 삭제
        planInvitationRepository.deleteByReceiverIdAndPlanId(memberId, planId);
    }

    public List<DayPlanDto> myplan(Long id) {
        List<PlanMember> planMembers = planMemberRepository.findByParticipantId(id);
        List<Plan> planList =  planMembers.stream()
                .map(PlanMember::getPlan)
                .collect(Collectors.toList());
        List<DayPlanDto> dayPlanDtoList = planList.stream()
                .map(plan -> {
                    List<DayPlan> dayPlans = plan.getDayPlans();
                    if (dayPlans != null && !dayPlans.isEmpty()) {
                        DayPlan firstDayPlan = dayPlans.get(0);
                        return DayPlanDto.builder()
                                .id(plan.getId())
                                .title(plan.getTitle())
                                .firstDay(plan.getFirstDay())
                                .lastDay(plan.getLastDay())
                                .locationList(firstDayPlan.getLocations().stream()
                                        .map(location -> LocationDto.builder()
                                                .lat(location.getLat())
                                                .lng(location.getLng())
                                                .imgUrl(location.getImgUrl())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build();
                    }
                    return null; // DayPlans가 비어있다면 null 반환
                })
                .filter(Objects::nonNull) // null 필터링
                .collect(Collectors.toList());
        // Dto로 변환해서 넣기
        return dayPlanDtoList;
    }
}
