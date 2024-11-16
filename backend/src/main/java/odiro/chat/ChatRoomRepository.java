package odiro.chat;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    // 필요한 경우 커스텀 쿼리를 추가할 수 있습니다.

    @Query("SELECT c FROM ChatRoom c JOIN c.users u WHERE u.id = :userId")
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);
}