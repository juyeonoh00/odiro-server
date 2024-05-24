package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import odiro.ExportData;
import odiro.PlanData;
import odiro.repository.PlanRepository;
import odiro.domain.Plan;
import odiro.service.PlanService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

//@Controller +ResponseBody = RestController
@Slf4j
@Controller
@RequiredArgsConstructor
public class PlanController {

    private final ObjectMapper objectMapper;
    private final PlanService planService;

    @ResponseBody
    @PostMapping("/members/{memberId}/InitializePlan")
    public ExportData initPlan(@PathVariable("memberId") Long memberId, @RequestBody PlanData inputdata) throws ParseException {


        ExportData exportdata = new ExportData();
        exportdata.setInitializerId(memberId);
        exportdata.setTitle(inputdata.getTitle());

        /* *내가쓴거
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date firstday = dateFormat.parse(inputdata.getFirstday());
        Date lastday = dateFormat.parse(inputdata.getLastday());

        Plan newPlan = new Plan(inputdata.getTitle(), firstday, lastday);

        Plan savedPlan = planRepository.save(newPlan);
        exportdata.setPlanId(savedPlan.getId());
        */

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate firstLocalDate = LocalDate.parse(inputdata.getFirstday(), dateFormatter);
            LocalDate lastLocalDate = LocalDate.parse(inputdata.getLastday(), dateFormatter);

            Date firstday = java.sql.Date.valueOf(firstLocalDate);
            Date lastday = java.sql.Date.valueOf(lastLocalDate);;

            Plan newPlan = new Plan(inputdata.getTitle(), firstday, lastday);
            Plan savedPlan = planService.initPlan(newPlan);

            if (savedPlan != null) {
                exportdata.setPlanId(savedPlan.getId());
            } else {
                log.error("Plan 저장 실패");
            }
        } catch (DateTimeParseException e) {
            log.error("날짜 형식 파싱 오류", e);
        }
        return exportdata;
    }


    @ResponseBody
    @GetMapping("/members/{memberId}/{planId}")
    public void viewPlan() {
        //지도, 달력, todo, comment, 댓글 반환
    }

    
}