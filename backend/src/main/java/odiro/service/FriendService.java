package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Friend;
import odiro.domain.member.Member;
import odiro.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}