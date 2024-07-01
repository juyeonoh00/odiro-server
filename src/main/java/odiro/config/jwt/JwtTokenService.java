package odiro.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;

//    private final PrincipalDetails principalDetails;
    public TokenDto generateToken(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
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
        return new TokenDto(accessToken, refreshToken);
    }

}
