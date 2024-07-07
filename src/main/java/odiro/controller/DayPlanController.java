package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.service.DayPlanService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
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