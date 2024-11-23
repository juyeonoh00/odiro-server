package odiro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.config.jwt.JwtUtil;
import odiro.config.jwt.TokenDto;
import odiro.config.oauth2.Oauth2TokenService;
import odiro.domain.member.Member;
import odiro.dto.member.UpdateMemberDto;
import odiro.dto.member.*;
import odiro.service.AwsService;
import odiro.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
        @PostMapping(value = "/signup")
    public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto) throws IOException {
            Member member = memberService.signUp(signUpDto);
            SignUpResponseDto res = SignUpResponseDto.toDto(member);
            //response 전달을 위한 dto 변환
            return ResponseEntity.status(HttpStatus.CREATED).body(res);

        }

    @Operation(summary = "로그인", description = "로그인후, access/refresh Token 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(memberService.signIn(signInRequestDto, response));
    }

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) throws NoSuchAlgorithmException, MessagingException, UnsupportedEncodingException {
        memberService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저 이름 중복 확인", description = "username 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/user/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam("username") @Valid String username) {
        Boolean duplicate = memberService.checkDuplicatedUsername(username);
        return ResponseEntity.ok(duplicate);
    }

    @Operation(summary = "이메일 인증 코드 입력", description = "이메일로 전송된 코드 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/emails/verifications")
    public ResponseEntity<Boolean> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        Boolean isVerified = memberService.verifiedCode(email, authCode);
        return ResponseEntity.ok(isVerified);
    }

    @Operation(summary = "비밀번호 확인", description = "비밀번호 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/api/confirm/password")
    public ResponseEntity<Boolean> verificationPassword(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody String password) {
        return ResponseEntity.ok(memberService.verificationPassword(principalDetails.getPassword(), password));
    }


    @GetMapping("/user/search/{username}")
    public ResponseEntity<List<UserSearchResponseDto>> searchUsers(@PathVariable("username") String username, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<UserSearchResponseDto> users = memberService.searchMembersByUsername(username);
        return ResponseEntity.ok(users);
    }


    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PatchMapping("/update")
    public ResponseEntity<MemberDto> updateMember(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute UpdateMemberDto request) throws IOException {
        MemberDto member = memberService.updateMember(principalDetails.getMember(), request);
        // dto 만들어서 조건 추가하기
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "마이페이지", description = "마이페이지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @GetMapping("/mypage")
    public ResponseEntity<Member> mypage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(memberService.mypage(principalDetails.getMember().getId()));
    }

    @Operation(summary = "로그아웃", description = "Acceess Token 인증 후, 현재 로그인중인 사용자 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PatchMapping("/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        memberService.logout(request);
        response.setHeader("Authorization", "");
    }
}
