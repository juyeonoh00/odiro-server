package odiro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.Comment;
import odiro.domain.Friend;
import odiro.domain.FriendRequest;
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

import java.util.List;
import java.util.stream.Collectors;

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
        FriendRequestResponseDto friendRequestId =  friendRequestService.sendFriendRequest(principalDetails.getMember().getId(), request.getReceiverId());

        return ResponseEntity.ok(friendRequestId);
    }

    // 친구 요청 수락하기
    @PostMapping("/friend/accept")
    public ResponseEntity<FriendAcceptResponseDto> acceptFriendRequest(
            @RequestBody FriendAcceptRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        FriendAcceptResponseDto friedshipId = friendRequestService.acceptFriendRequest(request.getSenderId(), principalDetails.getMember().getId());
        return ResponseEntity.ok(friedshipId);
    }

    //친구요청 목록 보기
    @GetMapping("/friend/wait/list")
    public ResponseEntity<List<FriendRequestListResponseDto>> getReceivedFriendRequests(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FriendRequestListResponseDto> friendRequests = friendRequestService.getReceivedFriendRequests(principalDetails.getMember().getId());

        return ResponseEntity.ok(friendRequests);
    }

    //친구 목록 보기
    @GetMapping("/friend/list")
    public ResponseEntity<List<FriendListResponseDto>> getFriendList(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<FriendListResponseDto> friends = friendService.getFriendList(principalDetails.getMember().getId());

        return ResponseEntity.ok(friends);
    }

    //친구 삭제
    @PostMapping("/friend/delete")
    public ResponseEntity<Void> removeFriend(@RequestBody FriendDeleteRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        friendService.removeFriend(principalDetails.getMember().getId(), request.getFriendId());
        return ResponseEntity.ok().build();
    }
}
