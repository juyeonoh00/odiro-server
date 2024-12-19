package odiro.dto.member;

import lombok.*;
import odiro.chat.domain.ChatRoom;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class LoginDto {
    //    private String grantType;
    private String accessToken;
    private String refreshToken;
    private List<ChatRoom> chatRooms;
//    private Long accessTokenExpiresIn;
}
