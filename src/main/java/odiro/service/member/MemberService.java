package odiro.service.member;

import lombok.RequiredArgsConstructor;
import odiro.config.auth.PrincipalDetails;
import odiro.config.jwt.JwtAuthenticationService;
import odiro.config.jwt.TokenDto;
import odiro.domain.member.Authority;
import odiro.dto.member.SignInRequestDto;
import odiro.dto.member.SignUpDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.member.Member;
import odiro.repository.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
    @Transactional
    public Member signUp(SignUpDto signUpDto) {
        signUpDto.setAuthority(Authority.valueOf("ROLE_USER"));
        //email과 닉네임에 대한 중복 확인
//        Optional <Member> memberEmail = memberRepository.findByEmail(signUpDto.getEmail());
//        Optional<Member> memberNickname = memberRepository.findByNickname(signUpDto.getNickname());
//        if (memberEmail.isPresent()) {
//            throw new DuplicateEmailException();
//        }
//
//        if (memberNickname.isPresent()) {
//            throw new DuplicateNicknameException();
//        }
        Member member = signUpDto.toEntity();
        member.passwordEncoding(passwordEncoder);
        memberRepository.save(member);
        return member;
    }
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public TokenDto login(SignInRequestDto signInRequestDto) {
//        UsernamePasswordAuthenticationToken authenticationToken = signInRequestDto.toAuthentication();
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        UserDetails userDetails = UserDetailsService.loadUserByUsername()
//        TokenDto tokenDto =

        return null;
    }
//    @Transactional
//    public JwtTokenDto login(SignInRequestDto signInRequestDto) {
//
//    }
}