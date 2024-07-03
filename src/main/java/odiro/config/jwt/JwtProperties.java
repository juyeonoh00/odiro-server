package odiro.config.jwt;


import org.springframework.beans.factory.annotation.Value;

public interface JwtProperties {
    long ACCESS_TOKEN_EXPIRATION_TIME = 600000; // 10분 (1/1000초)
    long REFRESH_TOKEN_EXPIRATION_TIME = 864000000; // 10일 (1/1000초)

    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER = "Authorization";
    String REFRESH_HEADER = "Authorization-Refresh";
}