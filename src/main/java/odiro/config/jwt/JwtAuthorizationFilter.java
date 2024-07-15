package odiro.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import odiro.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import java.io.IOException;
import java.util.Date;


//@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    @Value("${jwt.secret}")
//    private String secretKey="7ae96706cd6e6aade43a3b843bb3317f823ab927b64beb8d45558b3ac29f079dd36afe40f1646af31c334ccbb568cfe64b9e4e54a47aa5a5077796eca1e58075";
    private String secretKey;
    private final MemberRepository memberRepository;

//    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Autowired
    public JwtAuthorizationFilter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
            System.out.println("secret" + secretKey);
            System.out.println("doFilterInternal : 진입");
            String accessHeader = request.getHeader(JwtProperties.ACCESS_HEADER);
            String refreshHeader = request.getHeader(JwtProperties.REFRESH_HEADER); // 리프레시 토큰 헤더 추가

            if (request.getServletPath().equals("/signin") || accessHeader == null || !accessHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
                System.out.println("login : 진입");
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = accessHeader.replace(JwtProperties.TOKEN_PREFIX, "");

            try {
                // 토큰 검증
                String username = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken)
                        .getClaim("username").asString();
                System.out.println("???" + username);

                if (username != null) {
                    Member member = memberRepository.findByNickname(username)
                            .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

                    PrincipalDetails principalDetails = new PrincipalDetails(member);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (TokenExpiredException e) { // 액세스 토큰 만료 예외 처리 추가
                // 액세스 토큰이 만료된 경우 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
                System.out.println("-------------------------------------------리프레시토큰");
                if (refreshHeader != null && refreshHeader.startsWith(JwtProperties.TOKEN_PREFIX)) { // 리프레시 토큰 검증 추가
                    String refreshToken = refreshHeader.replace(JwtProperties.TOKEN_PREFIX, "");

                    try {
                        String username = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken)
                                .getSubject();

                        if (username != null) {
                            Member member = memberRepository.findByNickname(username)
                                    .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

                            String newAccessToken = JWT.create()
                                    .withSubject(member.getNickname())
                                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                                    .withClaim("id", member.getId())
                                    .withClaim("username", member.getNickname())
                                    .sign(Algorithm.HMAC512(secretKey));

                            // 새로운 액세스 토큰을 응답 헤더에 추가
                            response.setHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + newAccessToken);

                            PrincipalDetails principalDetails = new PrincipalDetails(member);
                            Authentication authentication = new UsernamePasswordAuthenticationToken(
                                    principalDetails,
                                    null,
                                    principalDetails.getAuthorities());

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (JWTVerificationException ex) { // 리프레시 토큰 검증 예외 처리 추가
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid refresh token");
                        return;
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Refresh token missing or invalid");
                    return;
                }
            }
        filterChain.doFilter(request, response);
    }

    //access token 검증
//
//    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        jwtAuthenticationService.extractAccessToken(request)
//                .filter(jwtAuthenticationService::isTokenValid)
//                .ifPresent(accessToken -> jwtAuthenticationService.extractEmail(accessToken)
//                        .ifPresent(email -> memberRepository.findByEmail(String.valueOf(email))
//                                .ifPresent(this::saveAuthentication)));
//
//        filterChain.doFilter(request, response);
//
//    }
//
//
////  access token 인증 성공 후 해당 유저 객체를 SecurityContextHolder에 담음
//    private void saveAuthentication(Member member) {
//        String password = member.getPassword();
//
//        // 소셜 로그인의 경우 password가 null이기 때문에, 랜덤 패스워드 부여
//        if (password == null) {
//            password = RandomPasswordGenerator.generateRandomPassword();
//        }
//
//        UserDetails userDetails = User.builder()
//                .username(member.getEmail())
//                .password(password)
//                .roles(member.getAuthority().name())
//                .build();
//
//
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null
//                        , authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    /**
//     * RefreshToken 검증 후, AccessToken과 RefreshToken 재발급
//     */
//    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
//        memberRepository.findByRefreshToken(refreshToken)
//                .ifPresent(user -> {
//                    String reIssuedRefreshToken = reIssueRefreshToken(user);
//                    jwtAuthenticationService.sendAccessAndRefreshToken(response, jwtAuthenticationService.createAccessToken(user.getEmail()),
//                            reIssuedRefreshToken);
//                });
//    }
//
//    /**
//     * RefreshToken 재발급 후, DB에 반영
//     */
//    private String reIssueRefreshToken(Member user) {
//        String reIssuedRefreshToken = jwtAuthenticationService.createRefreshToken();
//        user.setRefreshToken(reIssuedRefreshToken);
//        memberRepository.saveAndFlush(user);
//        return reIssuedRefreshToken;
//    }
}

