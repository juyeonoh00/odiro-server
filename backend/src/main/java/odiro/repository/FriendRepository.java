package odiro.repository;

import odiro.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Friend;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // senderId 또는 receiverId가 주어진 userId와 일치하는 Friend 목록 가져오기
    List<Friend> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId);

    // senderId와 receiverId로 친구 관계를 찾는 메서드
    Optional<Friend> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

}
