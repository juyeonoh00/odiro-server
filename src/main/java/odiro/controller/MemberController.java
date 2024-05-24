package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import odiro.ExportData;
import odiro.LoginRequest;
import odiro.PlanData;
import odiro.SignUpRequest;
import odiro.domain.Member;
import odiro.service.MemberService;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final ObjectMapper objectMapper;
    private final MemberService memberService;

//
    @ResponseBody
    @PostMapping("/members/signup")
    public SignUpRequest signUp(@RequestBody SignUpRequest request) throws ParseException {

        Member newMember = new Member();
        newMember.setUserId(request.getUser_id());
        newMember.setPassword(request.getPassword());
        newMember.setName(request.getName());
        newMember.setEmail(request.getEmail());

        Long memberId = memberService.join(newMember);

        SignUpRequest request1 = new SignUpRequest();
        request1.setId(newMember.getId());
        request1.setUser_id(newMember.getUserId());
        request1.setPassword(newMember.getPassword());
        request1.setEmail(newMember.getEmail());
        request1.setName(newMember.getName());

        return request1;
    }
//
//
//    @PostMapping("/members/login")
//    public void signUp(@RequestBody LoginRequest request) throws ParseException {
//
//        //메인페이지 html로 이동
//        return "mainpage";
//    }
//
//    @ResponseBody
//    @GetMapping("/members/{memberId}")
//    public void myPage(@PathVariable("memberId") Long memberId) {
//        Member findMember = new Member();
//        findMember = memberService.findById(memberId);
//
//        //member의 개인정보 모두 반환
//    }
//
//    @ResponseBody
//    @GetMapping("/members/{memberId}/plans")
//    public void myPlans(@PathVariable("memberId") Long memberId) {
//        Member findMember = new Member();
//        findMember = memberService.findById(memberId);
//        memberService.findPlans
//                //member가 생성한 플랜, 참여한 플랜 각각 리스트로 반환
//    }

}
