package odiro.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateChatDto {
    private Set<Long> memberIds;
    private String roomName = "default";
}
