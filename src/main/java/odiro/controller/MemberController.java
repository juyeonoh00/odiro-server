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
import odiro.domain.member.Member;
import odiro.dto.MemberDto;
import odiro.dto.UpdateUserRequest;
import odiro.dto.member.SignInRequestDto;
import odiro.dto.member.SignUpDto;
import odiro.dto.member.SignUpResponseDto;
import odiro.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;


    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
        @PostMapping(value = "/signup")
        public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto){
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
        Member member = memberService.signIn(signInRequestDto);
        TokenDto tokenDto = jwtUtil.generateToken(member, response);
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) throws NoSuchAlgorithmException, MessagingException, UnsupportedEncodingException {
        memberService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저 이름 중복 확인", description = "username 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/user/check-username")
    public ResponseEntity checkUsername(@RequestParam("username") @Valid String username) {
        memberService.checkDuplicatedUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "이메일 인증 코드 입력", description = "이메일로 전송된 코드 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        memberService.verifiedCode(email, authCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @PatchMapping("/user/update")
    public ResponseEntity<MemberDto> updateUser(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody UpdateUserRequest request) {
        MemberDto updatedMember = memberService.updateMember(principalDetails.getMember().getId(), request);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "로그아웃", description = "Acceess Token 인증 후, 현재 로그인중인 사용자 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @GetMapping("/logout")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        memberService.logout(request);
        response.setHeader("Authorization", "");
    }
}
