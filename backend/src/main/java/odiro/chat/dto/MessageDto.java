package odiro.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// 메시지 DTO
public class MessageDto {
    private String content;
    private String roomId;

    // Getter 및 Setter
}