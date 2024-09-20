package odiro.config.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class Oauth2TokenService {
    @Value("${spring.security.oauth2.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${spring.security.oauth2.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${spring.security.oauth2.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${spring.security.oauth2.kakao.authorization-grant-type}")
    private String KAKAO_AUTHORIZATION_GRANT_TYPE;
    @Value("${spring.security.oauth2.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;


    public OauthToken getKakaoAccessToken(String authorizationCode)  {

        // Set URI
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        // Set header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        // Set parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", KAKAO_AUTHORIZATION_GRANT_TYPE);
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", authorizationCode);
        params.add("client_secret", KAKAO_CLIENT_SECRET);

        // Set http entity
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri).headers(headers).body(params);
        ResponseEntity<String> responseEntity;

        try {
            // 토큰 받기
            responseEntity = restTemplate.exchange(requestEntity, String.class);
        } catch (Exception e) {
            log.error("[kakao] access token 발급 실패 "+e);
            throw (new RuntimeException("authorization code가 잘못되었습니다."));
        }

        // JSON String to OauthToken
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OauthToken oauthToken;

        try {
            oauthToken = objectMapper.readValue(responseEntity.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oauthToken;
    }


    public OAuthAttributes loadKakao(String accessToken, String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();
        // 유저 정보 불러오기 위한 requestEntity 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // 액세스 토큰을 사용하여 인증 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me", // 카카오 API 엔드포인트
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
        } catch (RuntimeException e) {
            log.error("[kakao] loadKakao 유저 정보 불러오기 실패");
            throw (new RuntimeException());
        }
        // JSON String to Object
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> attributes;
        try {
            attributes = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return OAuthAttributes.ofKakao(SocialType.KAKAO, attributes);
    }
}
