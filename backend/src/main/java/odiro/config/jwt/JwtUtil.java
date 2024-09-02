package odiro.config.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.config.auth.PrincipalDetailsService;
import odiro.config.jwt.exception.ErrorCode;
import odiro.config.jwt.exception.TokenExcption;
import odiro.domain.member.Member;
import odiro.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    private final PrincipalDetailsService principalDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;
    public String createAccessToken(Member member,HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(member.getUsername());
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        return accessToken;
    }

    public TokenDto generateToken(Authentication authentication, HttpServletResponse response){
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        // 권한들
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, authentication.getAuthorities())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, authentication.getAuthorities())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        refreshTokenRepository.save(new RefreshToken(userDetails.getMember().getId(), refreshToken, accessToken));
        return new TokenDto(accessToken, refreshToken);
    }
    public TokenDto generateToken(Member member, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(member.getUsername());
        // 권한들
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken, accessToken));
        return new TokenDto(accessToken, refreshToken);
    }


    // 에러 처리 세분화 필요
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException | TokenExcption e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken); // 토큰 복호화
        if (claims.get(JwtProperties.AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한정보가 없습니다.");
        }

        // 클레임에서 권한정보들 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtProperties.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체 생성해서 Authentication 리턴해준다.
        UserDetails principal = principalDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    public void validateRefreshToken(String token, HttpServletResponse response) throws IOException {
        log.error("💡 Access Token 이 만료되었습니다.");
        try
        {
            RefreshToken refreshTokenInfo = refreshTokenRepository.findByAccessToken(token).orElseThrow(()->new TokenExcption(ErrorCode.TOKEN_NOT_FOUND));
            String refreshToken = refreshTokenInfo.getRefreshToken();
            validateToken(refreshToken);
            Long memberId = refreshTokenInfo.getMemberId();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new TokenExcption(ErrorCode.USER_NOT_FOUNDED));
            // 리프레시 토큰 정보 기반 에세스 토큰 재발급
            String accessToken = createAccessToken(member, response);
            // 레디스에 저장
            refreshTokenRepository.save(new RefreshToken(memberId, refreshToken, accessToken));
            log.info("토큰 재생성");
        }catch (TokenExcption e) {
            // 리프레시 토큰 없으면
            log.info("리다이렉트");
            throw e;
        }
    }
}
