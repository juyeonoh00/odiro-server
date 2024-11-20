package odiro.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.member.Member;

@Getter
@Setter
public class MemberDto {
    private final String email;

    private final String username;

    private final String password;

    public MemberDto(Member member) {
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.password = member.getPassword();
    }
}
