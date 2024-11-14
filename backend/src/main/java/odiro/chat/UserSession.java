package odiro.chat;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UserSession {
    private final String userId;
    private final Set<String> joinedRooms;

    public UserSession(String userId) {
        this.userId = userId;
        this.joinedRooms = new HashSet<>();
    }

    public void joinRoom(String roomId) {
        joinedRooms.add(roomId);
    }

}