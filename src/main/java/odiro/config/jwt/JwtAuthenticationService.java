package odiro.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.config.auth.PrincipalDetailsService;
import odiro.dto.member.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

//@Configuration
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationService extends UsernamePasswordAuthenticationFilter{

    @Value("${jwt.secret}")
    private String secretKey ="7ae96706cd6e6aade43a3b843bb3317f823ab927b64beb8d45558b3ac29f079dd36afe40f1646af31c334ccbb568cfe64b9e4e54a47aa5a5077796eca1e58075";

    private final AuthenticationManager authenticationManager;

//    public JwtAuthenticationFilter(PrincipalDetails principalDetails, AuthenticationManager authenticationManager){
//        this.principalDetails = principalDetails;
//        this.authenticationManager = authenticationManager;
//    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입");

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        SignUpDto signUpDto = null;
        try {
            System.out.println("JwtAuthenticationFilter : 진입ㅅ");
            signUpDto = om.readValue(request.getInputStream(), SignUpDto.class);
        } catch (Exception e) {
            System.out.println("JwtAuthenticationFilter : 진입ㄷ");
            e.printStackTrace();
        }

        System.out.println("---------------------JwtAuthenticationFilter : "+signUpDto);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        signUpDto.getNickname(),
                        signUpDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");


        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : "+principalDetails.getAuthorities());
        return authentication;

    }





    // JWT Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication : 진입");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("username", principalDetails.getUsername())
                .withClaim("password", principalDetails.getPassword())
                .sign(Algorithm.HMAC512(secretKey));

        String refreshToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secretKey));

        System.out.println("successfulAuthentication : 토큰 생성");
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        response.addHeader(JwtProperties.REFRESH_HEADER, JwtProperties.TOKEN_PREFIX + refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"accessToken\": \"" + JwtProperties.TOKEN_PREFIX + accessToken + "\", \"refreshToken\": \"" + JwtProperties.TOKEN_PREFIX + refreshToken + "\"}");
    }
//
//    /**
//     * AccessToken 생성 메서드
//     */
//    public String createAccessToken(String email, Authentication authResult) {
//
//        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
//        Date now = new Date();
//
//        return JWT.create()
//                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
//                .withClaim("id", principalDetails.getMember().getId())
//                .withClaim("username", principalDetails.getMember().getNickname())
//                .sign(Algorithm.HMAC512(secretKey));
//    }
//
//    /**
//     * RefreshToken 생성 메서드
//     */
//    public String createRefreshToken() {
//        Date now = new Date();
//
//        return JWT.create()
//                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
//                .sign(Algorithm.HMAC512(secretKey));
//    }
//
//    /**
//     * 응답 메시지에 AccessToken 헤더에 실어서 보내기
//     */
//    public void sendAccessToken(HttpServletResponse response, String accessToken) {
//
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setHeader(JwtProperties.ACCESS_HEADER, accessToken);
//        log.info("재발급된 Access Token : {}", accessToken);
//    }
//
//    /**
//     * 응답 메시지에 AccessToken + RefreshToken 헤더에 실어서 보내기
//     */
//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        response.setHeader(JwtProperties.ACCESS_HEADER, accessToken);
//        response.setHeader(JwtProperties.REFRESH_HEADER, refreshToken);
//        log.info("Access Token, Refresh Token 헤더 설정 완료");
//    }
//    // header에서 refresh token 추출
//    public Optional<String> extractRefreshToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(JwtProperties.REFRESH_HEADER))
//                .filter(refreshToken -> refreshToken.startsWith(JwtProperties.TOKEN_PREFIX))
//                .map(refreshToken -> refreshToken.replace(JwtProperties.TOKEN_PREFIX, ""));
//    }
//
//    // header에서 refresh token 추출
//    public Optional<String> extractAccessToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(JwtProperties.ACCESS_HEADER))
//                .filter(refreshToken -> refreshToken.startsWith(JwtProperties.TOKEN_PREFIX))
//                .map(refreshToken -> refreshToken.replace(JwtProperties.TOKEN_PREFIX, ""));
//    }
//
//    //토큰 검증
//    public boolean isTokenValid(String token) {
//        try {
//            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
//            return true;
//        } catch (Exception e) {
//            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
//            return false;
//        }
//    }
//
//    public Optional<String> extractEmail(String accessToken) {
//        try {
//            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
//                    .build()
//                    .verify(accessToken)
//                    .getClaim("email")
//                    .asString());
//        } catch (Exception e) {
//            log.error("액세스 토큰이 유효하지 않습니다.");
//            return Optional.empty();
//        }
//    }
}
