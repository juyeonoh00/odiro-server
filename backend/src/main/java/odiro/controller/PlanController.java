package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.dto.dayPlan.DayPlanInDetailPage;
import odiro.dto.comment.CommentInDetailPage;
import odiro.dto.location.LocationInDetailPage;
import odiro.dto.location.WishLocationInDetailPage;
import odiro.dto.member.HomeResponse;
import odiro.dto.member.InitializerInDetailPage;
import odiro.dto.member.MemberInDetailPage;
import odiro.dto.memo.MemoInDetailPage;
import odiro.dto.plan.*;
import odiro.service.DayPlanService;
import odiro.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api")
public class PlanController {

    private final PlanService planService;
    private final DayPlanService dayPlanService;
    private final LocationService locationService;

    @GetMapping("/plan/myplan")
    public List<DayPlanDto> myplan( @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return planService.myplan(principalDetails.getMember().getId());
    }

    @PostMapping("/plan/create")
    public InitPlanResponse initPlan(@RequestBody InitPlanRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        Plan savedPlan = planService.initPlanV2(
                principalDetails.getMember().getId(), request);
        return new InitPlanResponse(savedPlan.getId());
    }

    @GetMapping("/plan/{planId}")
    public GetDetailPlanResponse getDetailPlan(@PathVariable("planId") Long planId) {

        Plan plan = planService.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found with id " + planId));


        Member initializer = plan.getInitializer();
        InitializerInDetailPage initializerResponse = new InitializerInDetailPage(
                initializer.getId(), initializer.getUsername(), initializer.getEmail(), initializer.getProfileImage());

        List<Member> participants = planService.findParticipantsByPlanId(planId);
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

        List<WishLocationInDetailPage> wishLocations = locationService.getWishLocationsByPlanId(planId);

        GetDetailPlanResponse response = new GetDetailPlanResponse(
                plan.getId(), plan.getTitle(), plan.getFirstDay(), plan.getLastDay(), initializerResponse, memberResponses, dayPlanResponses, plan.getPlanFilter(), wishLocations
        );

        return response;
    }
    @PutMapping("/plan/edit")
    public ResponseEntity<InitPlanResponse> editPlan(@RequestBody EditPlanRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Plan updatedPlan = planService.editPlan(request.getId(), request.getTitle(), request.getFirstDay(), request.getLastDay(), principalDetails.getMember());

        InitPlanResponse response = new InitPlanResponse(updatedPlan.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/plan/delete/{planId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        planService.deletePlan(planId,principalDetails.getMember());
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/plan/invite")
    public ResponseEntity<Void> inviteMember(@RequestBody PlanInviteRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        planService.inviteMember(principalDetails.getMember().getId(), request.getPlanId(), request.getReceiverId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/plan/wait/list")
    public ResponseEntity<List<PlanInvitationListResponse>> getPendingPlanInvitations(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long receiverId = principalDetails.getMember().getId(); // 로그인된 사용자 ID
        List<PlanInvitationListResponse> invitations = planService.getPendingInvitations(receiverId);
        return ResponseEntity.ok(invitations);
    }


    @PostMapping("/plan/join")
    public ResponseEntity<String> acceptInvitation(
            @RequestBody PlanInvitationAcceptRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId(); // 로그인된 사용자 ID
        Long planId = request.getId(); // 요청 본문에서 Plan ID 추출
        planService.acceptInvitation(memberId, planId);
        return ResponseEntity.ok("Invitation accepted and member added to the plan.");
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
