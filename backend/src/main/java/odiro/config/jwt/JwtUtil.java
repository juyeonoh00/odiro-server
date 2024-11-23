package odiro.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetailsService;
import odiro.config.redis.RedisService;
import odiro.exception.ErrorCode;
import odiro.config.jwt.exception.TokenExcption;
import odiro.domain.member.Member;
import odiro.exception.CustomException;
import odiro.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Key;
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
    private Key key;
    private final RedisService redisService;
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private final PrincipalDetailsService principalDetailsService;

    private final MemberRepository memberRepository;
    public String createAccessToken(Member member, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        return accessToken;
    }

    public TokenDto generateToken(Member member, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        // 권한들

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, member.getAuthority())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        return new TokenDto(accessToken, refreshToken);
    }


    // 에러 처리 세분화 필요
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException | TokenExcption e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
    }
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            log.error("💡 Access Token 이 만료되었습니다.");
            String accessToken = getJwtToken(request.getHeader(HttpHeaders.AUTHORIZATION));

            String userId = getIdByAccessToken(accessToken);

            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED));

            String refreshToken = redisService.getValues(userId);

            validateRefreshToken(refreshToken);

            String newToken = createAccessToken(member, response);

            response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + newToken);
        }catch (TokenExcption e) {
            // 리프레시 토큰 없으면
            throw e;
        }
    }
    public String getJwtToken(String token) throws RuntimeException {
        if (!StringUtils.hasText(token) && !token.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new RuntimeException("토큰이 없습니다.");
        }
        return token.substring(7);
    }
    public String getIdByAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            throw e;
        }
    }
    public void validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}
