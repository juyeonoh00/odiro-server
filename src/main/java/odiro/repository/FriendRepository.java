package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
}
