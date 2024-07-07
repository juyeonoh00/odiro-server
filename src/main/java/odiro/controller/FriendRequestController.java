package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.service.member.MemberService;
import org.springframework.stereotype.Controller;
import odiro.repository.FriendRequestRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class FriendRequestController {
    private final ObjectMapper objectMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberService memberService;
}
