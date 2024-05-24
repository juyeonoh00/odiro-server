package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import odiro.repository.FriendRequestRepository;
import odiro.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FriendRequestController {
    private final ObjectMapper objectMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberService memberService;
}
