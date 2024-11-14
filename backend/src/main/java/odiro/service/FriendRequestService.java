package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Friend;
import odiro.domain.FriendRequest;
import odiro.domain.member.Member;
import odiro.repository.FriendRepository;
import odiro.repository.FriendRequestRepository;
import odiro.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendService friendService; // FriendService 주입
    private final MemberRepository memberRepository;

    public Long sendFriendRequest(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);

        return friendRequestRepository.save(friendRequest).getId();
    }

    @Transactional
    public Long acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        // FriendService의 addFriend 메서드를 사용하여 친구 관계를 저장
        Long friendId = friendService.addFriend(friendRequest.getSender(), friendRequest.getReceiver());

        // 친구 요청 삭제
        friendRequestRepository.delete(friendRequest);

        return friendId;
    }
}
