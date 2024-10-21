package odiro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Location;
import odiro.domain.member.Member;
import odiro.dto.location.PostLocationRequest;
import odiro.dto.location.PostLocationResponse;
import odiro.dto.member.SignUpDto;
import odiro.dto.member.SignUpResponseDto;
import odiro.service.DayPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dayplan")
public class DayPlanController {

    private final DayPlanService dayPlanService;


    //플랜 생성시 자동 생성하도록 할수도 있음
    /*
    @PostMapping("/plan/{planId}/dayplan/create")
    public ResponseEntity<PostDayPlanResponse> writeComment(@PathVariable("planId") Long planId, @RequestBody PostDayPlanRequest request) {

        DayPlan dayPlan = dayPlanService.postDayPlan(planId, request.getDay());

        PostDayPlanResponse response = new PostDayPlanResponse(dayPlan.getId());

        return ResponseEntity.ok(response);
    }
    */


}