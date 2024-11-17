package odiro.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.domain.FriendRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestListResponseDto {
    private Long id;
    private String username;
    private String profileImg;

    public static FriendRequestListResponseDto fromEntity(FriendRequest friendRequest) {
        return new FriendRequestListResponseDto(
                friendRequest.getSender().getId(),
                friendRequest.getSender().getUsername(),
                friendRequest.getSender().getProfileImage()
        );
    }
}
