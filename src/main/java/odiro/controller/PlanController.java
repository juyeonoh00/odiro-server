package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Member;
import odiro.domain.PlanMember;
import odiro.dto.HomeRequest;
import odiro.dto.HomeResponse;
import odiro.dto.InitPlanRequest;
import odiro.repository.PlanMemberRepository;
import odiro.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import odiro.dto.InitPlanResponse;
import odiro.domain.Plan;
import odiro.service.PlanService;

import java.util.ArrayList;
import java.util.List;

//@Controller +ResponseBody = RestController
@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final MemberService memberService;

    @PostMapping("/plan/create")
    public ResponseEntity<InitPlanResponse> initPlan(@RequestBody InitPlanRequest inputData) {

        Plan savedPlan = planService.initPlanV2(
                inputData.getMemberId(), inputData.getTitle(), inputData.getFirstDay(), inputData.getLastDay());
        InitPlanResponse response = new InitPlanResponse(
                savedPlan.getId(), savedPlan.getInitializer().getId(), savedPlan.getTitle(), savedPlan.getFirstDay(), savedPlan.getLastDay());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public List<HomeResponse> homeForm(@RequestBody HomeRequest request) {

        List<Plan> planList = planService.findPlansByParticipantId(request.getMemberId());
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
