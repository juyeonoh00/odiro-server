package odiro.config.jwt;

import lombok.*;
import odiro.chat.domain.ChatRoom;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
//    private String grantType;
    private String accessToken;
    private String refreshToken;
//    private Long accessTokenExpiresIn;
}
