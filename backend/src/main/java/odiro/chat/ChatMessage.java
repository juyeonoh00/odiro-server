package odiro.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

}