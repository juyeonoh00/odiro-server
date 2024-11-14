package odiro.chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    // 필요한 경우 커스텀 쿼리를 추가할 수 있습니다.
}