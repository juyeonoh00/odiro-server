package odiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import odiro.domain.member.Member;

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
