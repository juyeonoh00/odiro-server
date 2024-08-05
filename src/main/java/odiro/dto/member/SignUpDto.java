package odiro.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import odiro.domain.member.Authority;
import odiro.domain.member.Member;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {



    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, message = "닉네임은 최소 2자 이상입니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    private String password;

    @JsonIgnore
    private Authority authority;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .authority(Authority.ROLE_USER)
                .emailVerified(true)
                .build();
    }
}
