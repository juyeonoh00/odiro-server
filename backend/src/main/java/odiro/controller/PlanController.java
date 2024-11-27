package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.dto.plan.*;
import odiro.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import odiro.domain.Plan;
import odiro.service.PlanService;

import java.util.List;

//@Controller +ResponseBody = RestController
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanController {

    private final PlanService planService;

    @GetMapping("/plan/myplan")
    public ResponseEntity<List<DayPlanDto>> myplan( @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<DayPlanDto> dayPlanDtoList = planService.myplan(principalDetails.getMember().getId());
        return ResponseEntity.ok(dayPlanDtoList);
    }

    @PostMapping("/plan/create")
    public ResponseEntity<InitPlanResponse> initPlan(@RequestBody InitPlanRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        InitPlanResponse savedPlan = planService.initPlanV2(
                principalDetails.getMember().getId(), request);
        return ResponseEntity.ok(savedPlan);

    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<GetDetailPlanResponse> getDetailPlan(@PathVariable("planId") Long planId) {
        GetDetailPlanResponse planDetails = planService.getDetailPlan(planId);
        return ResponseEntity.ok(planDetails);
    }
    @PutMapping("/plan/edit")
    public ResponseEntity<InitPlanResponse> editPlan(@RequestBody EditPlanRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        InitPlanResponse updatedPlan = planService.editPlan(request.getId(), request.getTitle(), request.getFirstDay(), request.getLastDay(), principalDetails.getMember());

        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/plan/delete/{planId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        planService.deletePlan(planId,principalDetails.getMember());
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/plan/invite")
    public ResponseEntity<Void> inviteMember(@RequestBody PlanInviteRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        planService.inviteMember(principalDetails.getMember().getId(), request.getPlanId(), request.getReceiverId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/plan/wait/list")
    public ResponseEntity<List<PlanInvitationListResponse>> getPendingPlanInvitations(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<PlanInvitationListResponse> invitations = planService.getPendingInvitations(principalDetails.getMember().getId());
        return ResponseEntity.ok(invitations);
    }


    @PostMapping("/plan/join")
    public ResponseEntity<String> acceptInvitation(
            @RequestBody PlanInvitationAcceptRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        planService.acceptInvitation(principalDetails.getMember().getId(), request.getId());
        return ResponseEntity.noContent().build();
    }


}
//    @Operation(summary = "Plan 카테고리 선택", description = "Plan 카테고리 선택")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK")
//    })
//    @GetMapping(value = "/create")
//    public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto){
//        Member member = memberService.signUp(signUpDto);
//        SignUpResponseDto res = SignUpResponseDto.toDto(member);
//        //response 전달을 위한 dto 변환
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(res);
//    }
//
//    @Operation(summary = "Plan 카테고리 수정", description = "Plan 카테고리 수정")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK")
//    })
//    @GetMapping(value = "/edit")
//    public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto){
//        Member member = memberService.signUp(signUpDto);
//        SignUpResponseDto res = SignUpResponseDto.toDto(member);
//        //response 전달을 위한 dto 변환
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(res);
//    }

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
