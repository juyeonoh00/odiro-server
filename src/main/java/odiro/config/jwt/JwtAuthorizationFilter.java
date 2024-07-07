package odiro.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import odiro.repository.member.MemberRepository;
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


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
//    private String secret = "7ae96706cd6e6aade43a3b843bb3317f823ab927b64beb8d45558b3ac29f079dd36afe40f1646af31c334ccbb568cfe64b9e4e54a47aa5a5077796eca1e58075";
    private String secret="7ae96706cd6e6aade43a3b843bb3317f823ab927b64beb8d45558b3ac29f079dd36afe40f1646af31c334ccbb568cfe64b9e4e54a47aa5a5077796eca1e58075";
    private final MemberRepository memberRepository;

//    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("secret"+secret);
        System.out.println("doFilterInternal : 진입");
        String header = request.getHeader(JwtProperties.ACCESS_HEADER);

//        "login" 요청, jwt 검증 x
//        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        if (request.getServletPath().equals("/signin")||header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            System.out.println("login : 진입");
            filterChain.doFilter(request, response);
            return;
        }

//        String refreshToken = jwtAuthenticationService.extractRefreshToken(request)
//                .filter(jwtAuthenticationService::isTokenValid)
//                .orElse(null);
//
//        // AccessToken 만료되어, refreshToken이 요청 헤더에 포함되어 있을 경우
//        if (refreshToken != null) {
//            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
//            return;
//        }
//
//        // AccessToken가 만료되지 않아, refreshToken이 없다.
//        if (refreshToken == null) {
//            checkAccessTokenAndAuthentication(request, response, filterChain);
//        }
//        System.out.println("header : " + header);




        String token = request.getHeader(JwtProperties.ACCESS_HEADER)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 집적접근해서 세션을 만들때 자동으로 UserDetailsService에 있는
        // loadByUsername이 호출됨.
        System.out.println("secret"+secret);
        String username = JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("username").asString();
        System.out.println("???"+username);

        if (username != null) {
            Member member = memberRepository.findByNickname(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));;

            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            PrincipalDetails principalDetails = new PrincipalDetails(member);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
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

