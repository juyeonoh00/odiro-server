package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Comment;
import odiro.domain.DayPlan;
import odiro.domain.Plan;
import odiro.dto.CommentRequest;
import odiro.dto.CommentResponse;
import odiro.dto.PostDayPlanRequest;
import odiro.dto.PostDayPlanResponse;
import odiro.service.CommentService;
import odiro.service.DayPlanService;
import odiro.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DayPlanController {

    private final DayPlanService dayPlanService;

    //플랜 생성시 자동 생성하도록 할수도 있음
    @PostMapping("/plan/{planId}/dayplan/create")
    public ResponseEntity<PostDayPlanResponse> writeComment(@PathVariable("planId") Long planId, @RequestBody PostDayPlanRequest request) {

        DayPlan dayPlan = dayPlanService.postDayPlan(planId, request.getDay());

        PostDayPlanResponse response = new PostDayPlanResponse(dayPlan.getId());

        return ResponseEntity.ok(response);
    }
}