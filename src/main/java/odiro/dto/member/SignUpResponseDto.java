package odiro.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odiro.domain.member.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {
    private String password;
    private String nickname;


    public static SignUpResponseDto toDto(Member member) {
        return new SignUpResponseDto(member.getPassword(), member.getNickname());
    }
}
