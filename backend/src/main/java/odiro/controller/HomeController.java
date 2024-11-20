package odiro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.service.HomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final HomeService homeService;
    @Operation(summary = "랜덤 Plan", description = "카테고리에 맞는 Plan을 보여줌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(value = "/plan")
    public ResponseEntity<List<DayPlanDto>> planFilteredList (@RequestParam(defaultValue = "0") String filterNum){
        List<DayPlanDto> planFilteredList = homeService.getplanFilteredList(filterNum);
        return ResponseEntity.status(HttpStatus.CREATED).body(planFilteredList);
    }

}
