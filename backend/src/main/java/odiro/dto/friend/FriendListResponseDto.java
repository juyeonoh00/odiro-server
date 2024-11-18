package odiro.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.domain.Friend;
import odiro.domain.member.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendListResponseDto {
    private Long id;
    private String username;
    private String profileImg;

    public static FriendListResponseDto fromEntity(Friend friend, Long userId) {
        // sender나 receiver 중 하나가 userId에 해당하는지 체크하여 friendMember를 설정
        Member friendMember = friend.getReceiver().getId().equals(userId)
                ? friend.getSender() : friend.getReceiver();

        return new FriendListResponseDto(
                friendMember.getId(),
                friendMember.getUsername(),
                friendMember.getProfileImage()
        );
    }
}