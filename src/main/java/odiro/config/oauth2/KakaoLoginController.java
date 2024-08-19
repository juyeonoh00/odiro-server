package odiro.config.oauth2;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import odiro.config.jwt.JwtUtil;
import odiro.config.jwt.TokenDto;
import odiro.domain.member.Member;
import odiro.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoLoginController {

    private final Oauth2TokenService oauth2TokenService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    @GetMapping("/kakao")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code, HttpServletResponse response) {
        OauthToken oauthToken = oauth2TokenService.getKakaoAccessToken(code);
        OAuthAttributes oAuthAttributes = oauth2TokenService.loadKakao(oauthToken.getAccessToken(), oauthToken.getRefreshToken());
        Member member = memberService.signUp(oAuthAttributes);
        TokenDto tokenDto = jwtUtil.generateToken(member, response);
        // member db에 저장
        // generator 호출
//        TokenDto tokenDto = jwtUtil.generateToken(authentication, response);
        return ResponseEntity.ok(tokenDto);
    }
}