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

