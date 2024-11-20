package odiro.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import odiro.domain.member.Authority;
import odiro.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class SignUpDto {

    // 이메일 중복 확인, 닉네임 중복 확인

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하입니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "비밀번호는 최소 하나의 대문자, 소문자와 숫자 또는 특수 문자를 포함해야 합니다.")
    private String password;

    @JsonIgnore
    private Authority authority;

    // 프로필 이미지가 없을 경우 기본 이미지 경로를 반환하는 메서드
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .authority(Authority.ROLE_USER)
                .profileImage("https://tennis-upload.s3.ap-northeast-2.amazonaws.com/avatars/1700428898409-person.png")
                .build();
    }
}
