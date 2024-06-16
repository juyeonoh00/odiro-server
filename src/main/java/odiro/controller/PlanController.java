package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Member;
import odiro.dto.InitPlanRequest;
import odiro.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import odiro.dto.InitPlanResponse;
import odiro.domain.Plan;
import odiro.service.PlanService;

//@Controller +ResponseBody = RestController
@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final MemberService memberService;

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

    @PostMapping("/plan/create")
    public ResponseEntity<InitPlanResponse> initPlan(@RequestBody InitPlanRequest inputData) {
        try {
            // 회원 검색
            Member member = memberService.findById(inputData.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            // 계획 생성 및 저장
            Plan newPlan = new Plan(member, inputData.getTitle(), inputData.getFirstday(), inputData.getLastday());
            Plan savedPlan = planService.initPlan(newPlan);

            if (savedPlan != null) {
                // 성공적으로 저장된 경우
                InitPlanResponse response = new InitPlanResponse(
                        savedPlan.getId(), savedPlan.getInitializer().getId(), savedPlan.getTitle(),
                        savedPlan.getFirstDay(), savedPlan.getLastDay());
                return ResponseEntity.ok(response);
            } else {
                // 저장 실패 시 로깅
                log.error("Plan 저장 실패");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            // 예외 발생 시 처리
            log.error("Plan 저장 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
}