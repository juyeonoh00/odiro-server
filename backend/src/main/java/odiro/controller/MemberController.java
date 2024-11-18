package odiro.controller;

import io.jsonwebtoken.Jwt;
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
import odiro.config.oauth2.OauthToken;
import odiro.domain.member.Member;
import odiro.dto.member.SignInRequestDto;
import odiro.dto.member.SignUpDto;
import odiro.dto.member.SignUpResponseDto;
import odiro.dto.member.UserSearchResponseDto;
import odiro.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    private Oauth2TokenService oauth2TokenService;

    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
        @PostMapping(value = "/signup")
        public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto){
            System.out.println("---------------------------------------");
            Member member = memberService.signUp(signUpDto);
            SignUpResponseDto res = SignUpResponseDto.toDto(member);
            //response 전달을 위한 dto 변환

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

    @Operation(summary = "로그인", description = "로그인후, access/refresh Token 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
//    @PostMapping("/auth/login")
//    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
//        return ResponseEntity.ok(memberService.login(memberRequestDto));
//    }
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
//    @Operation(summary = "카카오 로그인", description = "회원가입")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK")
//    })
//    @GetMapping("/kakao")
//    public ResponseEntity<> kakaoSignUp(@RequestParam("code") String code,HttpServletResponse response){
//        OauthToken oauthAccessToken = oauth2TokenService.getKakaoAccessToken(code);
//        jwtUtil.generateAuthToken(oauthAccessToken, response);
//        //(2)
//        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
//        String jwtToken = userService.SaveUserAndGetToken(oauthToken.getAccess_token());
//        //(3)
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
//        //(4)
//        return ResponseEntity.ok().headers(headers).body("success");
//    }


//    @Operation(summary = "로그아웃", description = "Acceess Token 인증 후, 현재 로그인중인 사용자 로그아웃")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "202", description = "Accepted")
//    })
//    @PatchMapping("/logout")
//    public void logout(HttpServletRequest request) {
//        String accessToken = JwtUtil.resolveAccessToken(request);
//        String ref = JwtUtil.resolveRefreshToken(request);
//        memberService.logout(ref, accessToken);
//    }


    @GetMapping("/user/search/{username}")
    public ResponseEntity<List<UserSearchResponseDto>> searchUsers(@PathVariable String username, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<UserSearchResponseDto> users = memberService.searchMembersByUsername(username);
        return ResponseEntity.ok(users);
    }






}
