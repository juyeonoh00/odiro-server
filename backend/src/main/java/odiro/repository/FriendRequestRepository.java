package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.FriendRequest;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<FriendRequest> findAllByReceiverId(Long receiverId);
}
