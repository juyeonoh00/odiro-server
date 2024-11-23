package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Friend;
import odiro.domain.FriendRequest;
import odiro.domain.member.Member;
import odiro.dto.friend.FriendAcceptResponseDto;
import odiro.dto.friend.FriendRequestListResponseDto;
import odiro.dto.friend.FriendRequestResponseDto;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import odiro.repository.FriendRepository;
import odiro.repository.FriendRequestRepository;
import odiro.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendService friendService;
    private final MemberRepository memberRepository;

    public FriendRequestResponseDto sendFriendRequest(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, "senderId  :"+senderId));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, "receiverId : " + receiverId));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);

        return new FriendRequestResponseDto(friendRequestRepository.save(friendRequest).getId());
    }


    public FriendAcceptResponseDto acceptFriendRequest(Long senderId, Long receiverId) {
        // senderId와 receiverId로 친구 요청 검색
        FriendRequest friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        // FriendService의 addFriend 메서드를 사용하여 친구 관계를 저장
        Long friendId = friendService.addFriend(friendRequest.getSender(), friendRequest.getReceiver());

        // 친구 요청 삭제
        friendRequestRepository.delete(friendRequest);

        return new FriendAcceptResponseDto(friendId);
    }

    public List<FriendRequestListResponseDto> getReceivedFriendRequests(Long userId) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByReceiverId(userId);
        return friendRequests.stream()
                .map(FriendRequestListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
