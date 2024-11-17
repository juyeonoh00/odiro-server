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
        Long requestId = friendRequestService.sendFriendRequest(principalDetails.getMember().getId(), request.getReceiverId());
        FriendRequestResponseDto response = new FriendRequestResponseDto(requestId);
        return ResponseEntity.ok(response);
    }

    // 친구 요청 수락하기
    @PostMapping("/friend/accept")
    public ResponseEntity<FriendAcceptResponseDto> acceptFriendRequest(
            @RequestBody FriendAcceptRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // Receiver ID는 인증된 사용자 정보에서 가져옵니다
        Long receiverId = principalDetails.getMember().getId();

        // Service 호출 시 senderId와 receiverId를 전달
        Long friendshipId = friendRequestService.acceptFriendRequest(request.getSenderId(), receiverId);

        FriendAcceptResponseDto response = new FriendAcceptResponseDto(friendshipId);
        return ResponseEntity.ok(response);
    }

    //친구요청 목록 보기
    @GetMapping("/friend/wait/list")
    public ResponseEntity<List<FriendRequestListResponseDto>> getReceivedFriendRequests(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();
        List<FriendRequest> friendRequests = friendRequestService.getReceivedFriendRequests(userId);

        // 엔티티를 DTO로 변환
        List<FriendRequestListResponseDto> response = friendRequests.stream()
                .map(FriendRequestListResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //친구 목록 보기
    @GetMapping("/friend/list")
    public ResponseEntity<List<FriendListResponseDto>> getFriendList(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();
        List<Friend> friends = friendService.getFriendList(userId);

        // 엔티티를 DTO로 변환
        List<FriendListResponseDto> response = friends.stream()
                .map(friend -> FriendListResponseDto.fromEntity(friend, userId)) // userId를 함께 전달
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //친구 삭제
    @PostMapping("/friend/delete")
    public ResponseEntity<Void> removeFriend(@RequestBody FriendDeleteRequestDto request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId(); // 현재 로그인된 사용자

        // 친구 삭제 로직 처리
        friendService.removeFriend(userId, request.getFriendId());

        return ResponseEntity.ok().build();  // 성공적으로 삭제되면 HTTP 200 반환
    }
}
