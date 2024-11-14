package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.Comment;
import odiro.dto.comment.CommentRequest;
import odiro.dto.comment.CommentResponse;
import odiro.dto.friend.*;
import odiro.service.FriendRequestService;
import odiro.service.FriendService;
import odiro.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import odiro.repository.FriendRequestRepository;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FriendRequestController {
    private final ObjectMapper objectMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberService memberService;

    private final FriendRequestService friendRequestService;
    private final FriendService friendService;

    // 친구 요청 보내기
    @PostMapping("/friend/request")
    public ResponseEntity<FriendRequestResponseDto> sendFriendRequest(@RequestBody FriendRequestRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long requestId = friendRequestService.sendFriendRequest(principalDetails.getMember().getId(), request.getReceiverId());
        FriendRequestResponseDto response = new FriendRequestResponseDto(requestId);
        return ResponseEntity.ok(response);
    }

    // 친구 요청 수락하기
    @PostMapping("/friend/accept")
    public ResponseEntity<FriendAcceptResponseDto> acceptFriendRequest(
            @RequestBody FriendAcceptRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long friendshipId = friendRequestService.acceptFriendRequest(request.getId());

        FriendAcceptResponseDto response = new FriendAcceptResponseDto(friendshipId);
        return ResponseEntity.ok(response);
    }

}
