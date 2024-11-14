package odiro.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {
    private String senderName;
    private String content;
    private String roomId;

    // 생성자, Getter 및 Setter
}