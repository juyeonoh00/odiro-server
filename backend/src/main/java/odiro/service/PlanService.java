package odiro.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import odiro.config.redis.RedisService;
import odiro.domain.DayPlan;
import odiro.domain.PlanMember;
import odiro.domain.member.Member;
import odiro.dto.comment.CommentInDetailPage;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.dto.dayPlan.DayPlanInDetailPage;
import odiro.dto.location.LocationDto;
import odiro.dto.location.LocationInDetailPage;
import odiro.dto.location.WishLocationInDetailPage;
import odiro.dto.member.InitializerInDetailPage;
import odiro.dto.member.MemberInDetailPage;
import odiro.dto.memo.MemoInDetailPage;
import odiro.dto.plan.GetDetailPlanResponse;
import odiro.dto.plan.InitPlanRequest;
import odiro.dto.plan.InitPlanResponse;
import odiro.dto.plan.PlanInvitationListResponse;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import odiro.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.Plan;
import odiro.domain.PlanInvitation;

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
    private final RedisService redisService;
    private final DayPlanRepository dayPlanRepository;
    private final LocationRepository locationRepotory;

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


    public InitPlanResponse initPlanV2(Long memberId, InitPlanRequest request) {
        // 멤버 검색
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, memberId));

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

        //DayPlan 생성
        LocalDateTime currentDateTime = request.getFirstDay();
        while (!currentDateTime.isAfter(request.getLastDay())) {
            postDayPlan(plan.getId(), currentDateTime);
            currentDateTime = currentDateTime.plusDays(1);
        }
        //저장된 플랜 반환
        return new InitPlanResponse(plan.getId());
    }
    public DayPlan postDayPlan(Long planId, LocalDateTime day) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));

        DayPlan savedDayPlan = new DayPlan();
        savedDayPlan.setPlan(plan);
        savedDayPlan.setDate(day);

        dayPlanRepository.save(savedDayPlan);
        return savedDayPlan;
    }


    public InitPlanResponse editPlan(Long planId, String title, LocalDateTime firstDay, LocalDateTime lastDay, Member member) {


        //Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));

        if(plan.getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(member.getId()))) {
            //Plan 수정 후 저장
            plan.setTitle(title);
            plan.setFirstDay(firstDay);
            plan.setLastDay(lastDay);
            Plan updatedPlan = planRepository.save(plan);

            return new InitPlanResponse(updatedPlan.getId());
        }
        else {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, member.getId());
        }
    }

    public void deletePlan(Long planId, Member member) {

        // Plan 검색
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));
        if(plan.getPlanMembers().contains(member)) {
            // Plan 삭제
            planRepository.delete(plan);
        }
    }

    // 초대 생성
    public PlanInvitation inviteMember(Long senderId, Long planId, Long receiverId) {
        // 초대 생성에 필요한 멤버 및 플랜 확인
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, "Sender : "+senderId));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, "Receiver : "+receiverId));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, "Plan : "+planId));

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
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, memberId));

        PlanMember planMember = new PlanMember();
        planMember.setParticipant(member);
        planMember.setPlan(plan);
        planMemberRepository.save(planMember);

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

    public GetDetailPlanResponse getDetailPlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));


        Member initializer = plan.getInitializer();
        InitializerInDetailPage initializerResponse = new InitializerInDetailPage(
                initializer.getId(), initializer.getUsername(), initializer.getEmail(), initializer.getProfileImage());

        List<Member> participants = findParticipantsByPlanId(planId);
        List<MemberInDetailPage> memberResponses = participants.stream()
                .map(member -> new MemberInDetailPage(member.getId(), member.getUsername(), member.getEmail(), member.getProfileImage()))
                .collect(Collectors.toList());


        //Location, memo,, comment는 day별로 묶어서

        List<DayPlanInDetailPage> dayPlanResponses = plan.getDayPlans().stream()
                .map(dayPlan -> {
                    List<LocationInDetailPage> locations = dayPlan.getLocations().stream()
                            .map(location -> new LocationInDetailPage( location.getLocationOrder(),
                                    location.getId(), location.getAddressName(), location.getKakaoMapId(), location.getPhone(),
                                    location.getPlaceName(), location.getPlaceUrl(), location.getLat(), location.getLng(),
                                    location.getRoadAddressName(), location.getImgUrl(),location.getCategoryGroupName()))
                            .collect(Collectors.toList());

                    List<MemoInDetailPage> memos = dayPlan.getMemos().stream()
                            .map(memo -> new MemoInDetailPage(memo.getId(), memo.getContent()))
                            .collect(Collectors.toList());

                    List<CommentInDetailPage> comments = dayPlan.getComments().stream()
                            .map(comment -> new CommentInDetailPage(comment.getId(), comment.getWriter().getId(), comment.getContent(), comment.getWriteTime()))
                            .collect(Collectors.toList());

                    return new DayPlanInDetailPage(dayPlan.getId(), dayPlan.getDate(), locations, memos, comments);
                })
                .collect(Collectors.toList());

        List<WishLocationInDetailPage> wishLocations = locationRepotory.getWishLocationsByPlanId(planId);
        return new GetDetailPlanResponse(
                plan.getId(), plan.getTitle(), plan.getFirstDay(), plan.getLastDay(), initializerResponse, memberResponses, dayPlanResponses, plan.getPlanFilter(), wishLocations
        );
    }
}
