package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.DayPlan;
import odiro.domain.Member;
import odiro.domain.PlanMember;
import odiro.dto.*;
import odiro.repository.PlanMemberRepository;
import odiro.service.DayPlanService;
import odiro.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import odiro.domain.Plan;
import odiro.service.PlanService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Controller +ResponseBody = RestController
@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final DayPlanService dayPlanService;

    @PostMapping("/plan/create")
    public ResponseEntity<InitPlanResponse> initPlan(@RequestBody InitPlanRequest inputData) {

        Plan savedPlan = planService.initPlanV2(
                inputData.getMemberId(), inputData.getTitle(), inputData.getFirstDay(), inputData.getLastDay());

        //DayPlan 생성
        LocalDateTime currentDateTime = inputData.getFirstDay();
        while (!currentDateTime.isAfter(inputData.getLastDay())) {
            DayPlan dayPlan = dayPlanService.postDayPlan(savedPlan.getId(), currentDateTime);
            currentDateTime = currentDateTime.plusDays(1);
        }

        InitPlanResponse response = new InitPlanResponse(
                savedPlan.getId(), savedPlan.getInitializer().getId(), savedPlan.getTitle(), savedPlan.getFirstDay(), savedPlan.getLastDay());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/home")
    public List<HomeResponse> homeForm(@PathVariable("memberId") Long memberId) {

        List<Plan> planList = planService.findPlansByParticipantId(memberId);
        return mapToHomeResponseList(planList);
    }

    private List<HomeResponse> mapToHomeResponseList(List<Plan> planList) {

        List<HomeResponse> responses = new ArrayList<>();
        for (Plan plan : planList) {
            HomeResponse response = new HomeResponse(
                    plan.getId(), plan.getTitle(),plan.getFirstDay(), plan.getLastDay());
            responses.add(response);
        }
        return responses;
    }

    @GetMapping("/plan/{planId}")
    public GetDetailPlanRespose getDetailPlan(@PathVariable("planId") Long planId) {

        Plan plan = planService.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found with id " + planId));


        Member initializer = plan.getInitializer();
        InitializerInDetailPage initializerResponse = new InitializerInDetailPage(
                initializer.getUserId(), initializer.getName(), initializer.getEmail(), initializer.getProfileImage());

        List<Member> participants = planService.findParticipantsByPlanId(planId);
        List<MemberInDetailPage> memberResponses = participants.stream()
                .map(member -> new MemberInDetailPage(member.getUserId(), member.getName(), member.getEmail(), member.getProfileImage()))
                .collect(Collectors.toList());


        //

        List<LocationInDetailPage> locationResponses = plan.getDayPlans().stream()
                .flatMap(dayPlan -> dayPlan.getLocations().stream())
                .map(location -> new LocationInDetailPage(
                        location.getId(), location.getAddressName(), location.getKakaoMapId(), location.getPhone(),
                        location.getPlaceName(), location.getPlaceUrl(), location.getLat(), location.getLng(),
                        location.getRoadAddressName(), location.getCategoryGroupName(), location.getImgUrl()))
                .collect(Collectors.toList());

        List<MemoInDetailPage> memoResponses = plan.getDayPlans().stream()
                .flatMap(dayPlan -> dayPlan.getMemos().stream())
                .map(memo -> new MemoInDetailPage(memo.getId(), memo.getContent()))
                .collect(Collectors.toList());

        List<CommentInDetailPage> commentResponses = plan.getDayPlans().stream()
                .flatMap(dayPlan -> dayPlan.getComments().stream())
                .map(comment -> new CommentInDetailPage(comment.getId(),comment.getWriter().getId(), comment.getContent(), comment.getWriteTime()))
                .collect(Collectors.toList());

        GetDetailPlanRespose response = new GetDetailPlanRespose(
                plan.getId(), plan.getTitle(), plan.getFirstDay(), plan.getLastDay(),
                initializerResponse, memberResponses, locationResponses, memoResponses, commentResponses
        );

        return response;
    }



}


//    @PostMapping("/plan/create")
//    public InitPlanResponse initPlan(@RequestBody InitPlanData inputData) {
//
//        Member member = memberService.findById(inputData.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
//
//        Plan newPlan = new Plan(member, inputData.getTitle(), inputData.getFirstday(), inputData.getLastday());
//        Plan savedPlan = planService.initPlan(newPlan);
//
//        if (savedPlan != null) {
//            InitPlanResponse response = new InitPlanResponse(
//                    savedPlan.getId(), savedPlan.getInitializer().getId(), savedPlan.getTitle(), savedPlan.getFirstDay(), savedPlan.getLastDay());
//            return response;
//        } else {
//            log.error("Plan 저장 실패");
//        }
//    }
