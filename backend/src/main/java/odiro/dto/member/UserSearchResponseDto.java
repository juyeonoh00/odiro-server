package odiro.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.domain.member.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponseDto {
    private Long id;
    private String username;
    private String profileImg;

    // Entity -> DTO 변환 메서드
    public static UserSearchResponseDto fromEntity(Member member) {
        return new UserSearchResponseDto(
                member.getId(),
                member.getUsername(),
                member.getProfileImage()
        );
    }
}