package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Friend;
import odiro.domain.member.Member;
import odiro.dto.friend.FriendListResponseDto;
import odiro.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public Long addFriend(Member sender, Member receiver) {
        Friend friend = new Friend();
        friend.setSender(sender);
        friend.setReceiver(receiver);

        return friendRepository.save(friend).getId();
    }

    // 친구 목록 가져오기
    public List<FriendListResponseDto> getFriendList(Long userId) {
        // 친구 목록을 가져오는 로직:
        // 1. sender가 userId인 경우 또는 receiver가 userId인 경우
        List<Friend> friends = friendRepository.findAllBySenderIdOrReceiverId(userId, userId);

        return friends.stream()
                .map(friend -> FriendListResponseDto.fromEntity(friend, userId)) // userId를 함께 전달
                .collect(Collectors.toList());
    }

    // 친구 삭제
    public void removeFriend(Long userId, Long friendId) {
        // senderId와 receiverId로 친구 관계를 찾음
        Friend friend = friendRepository.findBySenderIdAndReceiverId(userId, friendId)
                .orElseGet(() -> friendRepository.findBySenderIdAndReceiverId(friendId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("친구 관계가 존재하지 않습니다.")));

        // 해당 친구 관계 삭제
        friendRepository.delete(friend);
    }
}