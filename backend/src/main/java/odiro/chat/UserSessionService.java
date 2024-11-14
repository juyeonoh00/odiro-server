package odiro.chat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
    private final Map<String, UserSession> userSessions = new HashMap<>();

    public UserSession getOrCreateSession(String userId) {
        return userSessions.computeIfAbsent(userId, UserSession::new);
    }

    public Set<String> getJoinedRooms(String userId) {
        UserSession session = userSessions.get(userId);
        return session != null ? session.getJoinedRooms() : Set.of();
    }
}