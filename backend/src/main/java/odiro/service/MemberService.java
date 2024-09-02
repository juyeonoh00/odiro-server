
package odiro.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.jwt.RefreshTokenService;
import odiro.config.oauth2.OAuthAttributes;
import odiro.config.redis.RedisService;
import odiro.domain.member.Authority;
import odiro.dto.MemberDto;
import odiro.dto.UpdateUserRequest;
import odiro.dto.member.SignInRequestDto;
import odiro.dto.member.SignUpDto;
import odiro.exception.member.EmailAlreadyExistsException;
import odiro.exception.member.UsernameAlreadyExistsException;
import odiro.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import odiro.domain.member.Member;
import odiro.config.email.EmailService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;
    private final RefreshTokenService refreshTokenService;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
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
        Member member = signUpDto.toEntity();
        member.passwordEncoding(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    // 소셜 로그인
    @Transactional
    public Member signUp(OAuthAttributes oAuthAttributes) {
//        oAuthAttributes.setAuthority(Authority.valueOf("ROLE_USER"));
        Member member = oAuthAttributes.toEntity();
        memberRepository.save(member);
        return member;
    }


    public void sendCodeToEmail(String toEmail) throws NoSuchAlgorithmException, MessagingException, UnsupportedEncodingException {
        this.checkDuplicatedEmail(toEmail);
        String title = "[ODIRO] 이메일 인증 번호";
        String authCode = this.createCode();
        emailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    public void verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if (!authResult) {
            log.debug("MemberService.createCode() exception occur");
        }
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new EmailAlreadyExistsException(email);

        }
    }
    public boolean checkDuplicatedUsername(String username) {
        Optional<Member> member = memberRepository.findByusername(username);
        return member.isPresent();
    }
    private String createCode() throws NoSuchAlgorithmException {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw e;
        }
    }

    public MemberDto updateMember(Long id, UpdateUserRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (request.getUsername() != null) {
            Optional<Member> checkUser = memberRepository.findByusername(request.getUsername());
            if (checkUser.isPresent()) {
                throw new UsernameAlreadyExistsException(member.getUsername());
            }
            member.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            Optional<Member> checkUser = memberRepository.findByEmail(request.getEmail());
            if (checkUser.isPresent()) {
                throw new EmailAlreadyExistsException(member.getEmail());
            }
            member.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            member.setPassword(request.getPassword());
            member.passwordEncoding(passwordEncoder);
        }
        memberRepository.save(member);
        return new MemberDto(member);
    }
    public Member signIn(SignInRequestDto signInRequestDto) throws Exception {
        Member member = memberRepository.findByusername(signInRequestDto.getUsername()).orElseThrow(()-> new
                RuntimeException("user가 존재하지 않습니다."));
        if (member == null || !passwordEncoder.matches(signInRequestDto.getPassword(), member.getPassword())) {
            // 비밀번호가 일치하지 않거나 사용자가 존재하지 않으면 예외 발생
            throw new Exception("Invalid username or password.");
        }
        return member;
    }

    public void logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        refreshTokenService.removeRefreshToken(authorizationHeader.substring(7));
    }
}

