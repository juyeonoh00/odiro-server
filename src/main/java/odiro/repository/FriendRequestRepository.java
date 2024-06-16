package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.FriendRequest;
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
