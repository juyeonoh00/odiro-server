package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
